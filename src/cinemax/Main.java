package cinemax;
import java.sql.*;
import java.util.*;

import static cinemax.Seatconfig.print;
import static cinemax.Seatconfig.printff;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            while (running) {
                System.out.println("WELCOME TO CINEMAX");
                System.out.println("1. Book ticket");
                System.out.println("2. View available tickets");
                System.out.println("3. View available movies");
                System.out.println("4. View previous watched movies");
                System.out.println("5. Manage Theatre Branches");
                System.out.println("6. Exit");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        bookTicket(conn, sc);
                        break;
                    case 2:
                        availableTicketsForShow(conn, sc);
                        break;
                    case 3:
                        showAvailableMovies(conn, sc);
                        break;
                    case 4:
                        watchedMovies(conn, sc);
                        break;
                    case 5:
                        manageTheatreBranches(conn,sc);
                        break;
                    case 6:
                        running = false;
                        System.out.println("Exiting CINEMAX. Have a nice day!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sc.close();
    }
    static void bookTicket(Connection conn, Scanner sc) throws SQLException {
        int branchId = selectBranch(sc);
        if (branchId == -1) return;
        System.out.println("Enter your userId:");
        int userId = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter your Name:");
        String name = sc.nextLine();
        System.out.println("Enter Email ID:");
        String mail = sc.nextLine();
        System.out.println("Enter Phone Number:");
        String phone = sc.nextLine();
        System.out.println("Enter number of tickets to book:");
        int noOfTickets = sc.nextInt();
        List<Integer> seatNumbers = new ArrayList<>();
        System.out.println("Enter seat numbers (comma separated):");
        sc.nextLine();
        String[] seatsInput = sc.nextLine().split(",");
        for (String seat : seatsInput) {
            seatNumbers.add(Integer.parseInt(seat.trim()));
        }
        showAvailableMoviesByBranch(conn, branchId);
        System.out.println("Select Show (1 to 44):");
        int showChoice = sc.nextInt();
        if (showChoice < 1 || showChoice > 44) {
            System.out.println("Invalid show selection.");
            return;
        }
        ticketBooking(conn, userId, name, mail, phone, seatNumbers, showChoice);
    }
    static int selectBranch(Scanner sc) throws SQLException {
        System.out.println("Select Theatre Branch:");
        List<TheatreBranch> branches = TheatreBranch.getAll();
        if (branches.isEmpty()) {
            System.out.println("No branches available.");
            return -1;
        }
        for (TheatreBranch branch : branches) {
            System.out.println("Branch ID: " + branch.branchId + " | Branch Name: " + branch.branchName + " | Location: " + branch.location);
        }
        System.out.println("Enter Branch ID:");
        int branchId = sc.nextInt();
        return branchId;
    }
    public static void showAvailableMoviesByBranch(Connection conn, int branchId) throws SQLException {
        String query = "SELECT * FROM Movieshow WHERE branchId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, branchId);
            ResultSet rs = pstmt.executeQuery();
            //int movieId = rs.getInt("movieId");
           // String queryq="SELECT * from movie";
            System.out.println("Available Movies:");
            System.out.println("______________________");
            while (rs.next()) {
                System.out.println("Show ID: " + rs.getInt("showid"));
                System.out.println("Movie: " + rs.getString("movieName"));
                System.out.println("Screen ID: " + rs.getInt("screenId"));
                System.out.println("Start Time: " + rs.getTime("starttime"));
                System.out.println("End Time: " + rs.getTime("endtime"));
                System.out.println("Price: $" + rs.getDouble("price"));
                System.out.println("Available Tickets: " + rs.getInt("availableTickets"));
                System.out.println("---------------");
            }
        }
    }
    static void ticketBooking(Connection conn, int userId, String name, String mail, String phone, List<Integer> seatNumbers, int showId) throws SQLException {
        String query = "SELECT showid, movieName, movieId, screenId, price, availableTickets FROM Movieshow WHERE showid = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, showId);
        ResultSet rs = pstmt.executeQuery();
        if (!rs.next()) {
            System.out.println("Show not found.");
            return;
        }
        int screenId = rs.getInt("screenId");
        String movieName = rs.getString("movieName");
        double baseAmount = rs.getDouble("price");
        int availableSeats = rs.getInt("availableTickets");

        if (availableSeats < seatNumbers.size()) {
            System.out.println("Not enough available seats.");
            return;
        }

        double entertainment = baseAmount * 0.02;
        double GST = baseAmount * 0.18;
        double totalAmount = baseAmount + entertainment + GST;
        int ticketId = generateTicketId(conn);
        int paymentId = ticketId;
        String transactionsId = "TRANS" + ticketId;
        String paymentStatus = "Success";
        String paymentQuery = "INSERT INTO Payment (paymentId, ticketID, amount, paymentMethod, paymentDate, paymentStatus, transactionsId, payerName, payerEmail) VALUES (?, ?, ?, ?, CURDATE(), ?, ?, ?, ?)";
        try (PreparedStatement psmt = conn.prepareStatement(paymentQuery)) {
            psmt.setInt(1, paymentId);
            psmt.setInt(2, ticketId);
            psmt.setDouble(3, totalAmount);
            psmt.setString(4, "Credit Card");
            psmt.setString(5, paymentStatus);
            psmt.setString(6, transactionsId);
            psmt.setString(7, name);
            psmt.setString(8, mail);
            psmt.executeUpdate();
        }
        String ticketQuery = "INSERT INTO Ticket (ticketId, userId, movieName, screenId, numberOfTickets, baseAmount, entertainment, GST, totalAmount, ticketDate, ticketTiming, paymentId, paymentStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURDATE(), CURTIME(), ?, ?)";
        try (PreparedStatement tsmt = conn.prepareStatement(ticketQuery)) {
            tsmt.setInt(1, ticketId);
            tsmt.setInt(2, userId);
            tsmt.setString(3, movieName);
            tsmt.setInt(4, screenId);
            tsmt.setInt(5, seatNumbers.size());
            tsmt.setDouble(6, baseAmount);
            tsmt.setDouble(7, entertainment);
            tsmt.setDouble(8, GST);
            tsmt.setDouble(9, totalAmount);
            tsmt.setInt(10, paymentId);
            tsmt.setString(11, paymentStatus);
            tsmt.executeUpdate();
        }
        String updateQuery = "UPDATE Movieshow SET availableTickets = ? WHERE showid = ?";
        try (PreparedStatement usmt = conn.prepareStatement(updateQuery)) {
            usmt.setInt(1, availableSeats - seatNumbers.size());
            usmt.setInt(2, showId);
            usmt.executeUpdate();
        }
        System.out.println("------ TICKET ------");
        System.out.println("Ticket ID: " + ticketId);
        System.out.println("Customer Name: " + name);
        System.out.println("Movie: " + movieName);
        System.out.println("Screen: " + screenId);
        System.out.println("Number of Tickets: " + seatNumbers.size());
        System.out.println("Base Amount: $" + baseAmount);
        System.out.println("Entertainment Tax: $" + entertainment);
        System.out.println("GST: $" + GST);
        System.out.println("Total Amount: $" + totalAmount);
        System.out.println("--------------------");
        System.out.println("Ticket successfully booked.");
    }
    static int generateTicketId(Connection conn) throws SQLException {
        String query = "SELECT MAX(ticketId) FROM Ticket";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        }
        return 1;
    }
    static void availableTicketsForShow(Connection conn, Scanner sc) throws SQLException {
        showAvailableMovies(conn, sc);
        System.out.println("Enter Show ID to view available tickets:");
        int showId = sc.nextInt();
        String query = "SELECT * FROM Movieshow WHERE showid = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, showId);
        ResultSet rs = pstmt.executeQuery();
        int screenid=0;
        if (rs.next()) {
            System.out.println("___________________________________________");
            System.out.println("Show ID: " + rs.getInt("showid"));
            System.out.println("Movie: " + rs.getString("movieName"));
            System.out.println("Screen ID: " + rs.getInt("screenId"));
            screenid=rs.getInt("screenId");
            System.out.println("Start Time: " + rs.getTime("starttime"));
            System.out.println("End Time: " + rs.getTime("endtime"));
            System.out.println("Price: $" + rs.getInt("price"));
            System.out.println("Available Tickets: " + rs.getInt("availableTickets"));
            System.out.println("___________________________________________");
            Seatconfig.retrieveAndPrintScreenLayout(conn,screenid);

        } else {
            System.out.println("No details found for the selected show.");
            System.out.println("___________________________________________");
        }
    }
    static void showAvailableMovies(Connection conn, Scanner sc) throws SQLException {
        String query = "SELECT * FROM Movieshow";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("Available Movies:");
        System.out.println("______________________");
        while (rs.next()) {
            System.out.println("Show ID: " + rs.getInt("showid"));
            System.out.println("Movie: " + rs.getString("movieName"));
            System.out.println("Screen ID: " + rs.getInt("screenId"));
            System.out.println("Start Time: " + rs.getTime("starttime"));
            System.out.println("End Time: " + rs.getTime("endtime"));
            System.out.println("Price: $" + rs.getInt("price"));
            System.out.println("Available Tickets: " + rs.getInt("availableTickets"));
            System.out.println("---------------");
        }
    }
    static void watchedMovies(Connection conn, Scanner sc) throws SQLException {
        System.out.println("Enter your userId to view watched movies:");
        int userId = sc.nextInt();
        String query = "SELECT * FROM ticket WHERE userId = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();
        System.out.println("Watched Movies:");
        while (rs.next()) {
            System.out.println("Movie: " + rs.getString("movieName"));
            System.out.println("Watched Date: " + rs.getDate("ticketDate"));
            System.out.println("---------------");
        }
    }
    static void manageTheatreBranches(Connection conn,Scanner sc) throws SQLException {
        boolean managing = true;
        while (managing) {
            System.out.println("Manage Theatre Branches");
            System.out.println("1. Add Theatre Branch");
            System.out.println("2. View All Theatre Branches");
            System.out.println("3. Update Theatre Branch");
            System.out.println("4. Delete Theatre Branch");
            System.out.println("5.Made screen Configration ");
            System.out.println("6. Go Back");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    addTheatreBranch(sc);
                    break;
                case 2:
                    TheatreBranch.printAll();
                    break;
                case 3:
                    updateTheatreBranch(sc);
                    break;
                case 4:
                    deleteTheatreBranch(sc);
                    break;
                case 5:
                    Seatconfiguration();
                    break;
                case 6:
                    managing = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    public static void Seatconfiguration() throws SQLException {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the branch id:");
        int branchId=sc.nextInt();
        System.out.println("Enter the screen Id:");
        int screenId=sc.nextInt();
        System.out.println("enter no of seats");
        int n=sc.nextInt();
        if(n%10!=0){
            System.out.println("Invalid no of seats");
            return;
        }
        System.out.print("Enter the number of seats per row");
        int rowseat=sc.nextInt();
        if(rowseat%5!=0 && rowseat%10!=0){
            System.out.print("ENTER VALID NO OF SEATS");
            return;
        }
        int per=100;
        System.out.println("Enter the Premium class percentage:");
        int a1=sc.nextInt();
        if(a1%10!=0){
            System.out.println("enter valid percentage");
            return;
        }
        System.out.println("Enter the standard class percentage:");
        int b1=sc.nextInt();
        if(b1%10!=0){
            System.out.println("enter valid percentage");
            return;
        }
        System.out.println("Enter the normal class percentage:");
        int c1=sc.nextInt();
        if(c1%10!=0){
            System.out.println("enter valid percentage");
            return;
        }
        System.out.println("enter the path size:");
        int path=sc.nextInt();
        System.out.println("enter the cpath1 starting:");
        int cpath1=sc.nextInt();
        System.out.println("enter the cpath2 starting");
        int cpath2=sc.nextInt();
        System.out.println("enter the speaker location");
        int speaker=sc.nextInt();
        printff(n,rowseat,path,a1,b1,c1,cpath1,cpath2,speaker);
        Seatconfig seatConfig = new Seatconfig (path, a1, b1, c1, screenId,speaker);
        seatConfig.insertIntoDatabase( screenId,path, a1, b1, c1, speaker,cpath1,cpath2);


    }
    static void addTheatreBranch(Scanner sc) throws SQLException {
        System.out.println("Enter Theatre Branch ID:");
        int branchId = sc.nextInt();
        sc.nextLine(); // Consume newline
        System.out.println("Enter Branch Name:");
        String branchName = sc.nextLine();
        System.out.println("Enter Location:");
        String location = sc.nextLine();
        System.out.println("Enter Total Screens:");
        int totalScreens = sc.nextInt();
        TheatreBranch branch = new TheatreBranch(branchId, branchName, location, totalScreens);
        TheatreBranch.save(branch);
        System.out.println("Theatre Branch added successfully.");
    }
    static void updateTheatreBranch(Scanner sc) throws SQLException {
        System.out.println("Enter Theatre Branch ID to update:");
        int branchId = sc.nextInt();
        sc.nextLine();
        TheatreBranch branch = TheatreBranch.getById(branchId);
        if (branch == null) {
            System.out.println("Theatre Branch not found.");
            return;
        }
        System.out.println("Enter new Branch Name (current: " + branch.branchName + "):");
        String branchName = sc.nextLine();
        System.out.println("Enter new Location (current: " + branch.location + "):");
        String location = sc.nextLine();
        System.out.println("Enter new Total Screens (current: " + branch.totalScreens + "):");
        int totalScreens = sc.nextInt();
        branch.branchName = branchName;
        branch.location = location;
        branch.totalScreens = totalScreens;
        TheatreBranch.update(branch);
        System.out.println("Theatre Branch updated successfully.");
    }
    static void deleteTheatreBranch(Scanner sc) throws SQLException {
        System.out.println("Enter Theatre Branch ID to delete:");
        int branchId = sc.nextInt();
        TheatreBranch.delete(branchId);
        System.out.println("Theatre Branch deleted successfully.");
    }
}
