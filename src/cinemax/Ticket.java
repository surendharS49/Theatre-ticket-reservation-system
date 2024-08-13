package cinemax;

import cinemax.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
    int ticketId;
    int customerId;
    String movieName;
    int screenId;
    int numberOfTickets;
    double baseAmount;
    double entertainment;
    double GST;
    double totalAmount;
    Date ticketDate;
    Time ticketTiming;
    String paymentId;

    public Ticket(int ticketId, int customerId, String movieName, int screenId, int numberOfTickets, double baseAmount, double entertainment, double GST, double totalAmount, Date ticketDate, Time ticketTiming, String paymentId) {
        this.ticketId = ticketId;
        this.customerId = customerId;
        this.movieName = movieName;
        this.screenId = screenId;
        this.numberOfTickets = numberOfTickets;
        this.baseAmount = baseAmount;
        this.entertainment = entertainment;
        this.GST = GST;
        this.totalAmount = totalAmount;
        this.ticketDate = ticketDate;
        this.ticketTiming = ticketTiming;
        this.paymentId = paymentId;
    }

    public static void save(Ticket ticket) throws SQLException {
        String query = "INSERT INTO Ticket (ticketId, customerId, movieName, screenId, numberOfTickets, baseAmount, entertainment, GST, totalAmount, ticketDate, ticketTiming, paymentId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ticket.ticketId);
            stmt.setInt(2, ticket.customerId);
            stmt.setString(3, ticket.movieName);
            stmt.setInt(4, ticket.screenId);
            stmt.setInt(5, ticket.numberOfTickets);
            stmt.setDouble(6, ticket.baseAmount);
            stmt.setDouble(7, ticket.entertainment);
            stmt.setDouble(8, ticket.GST);
            stmt.setDouble(9, ticket.totalAmount);
            stmt.setDate(10, ticket.ticketDate);
            stmt.setTime(11, ticket.ticketTiming);
            stmt.setString(12, ticket.paymentId);
            stmt.executeUpdate();
        }
    }

    public static List<Ticket> getAll() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM Ticket";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Ticket ticket = new Ticket(
                        rs.getInt("ticketId"),
                        rs.getInt("customerId"),
                        rs.getString("movieName"),
                        rs.getInt("screenId"),
                        rs.getInt("numberOfTickets"),
                        rs.getDouble("baseAmount"),
                        rs.getDouble("entertainment"),
                        rs.getDouble("GST"),
                        rs.getDouble("totalAmount"),
                        rs.getDate("ticketDate"),
                        rs.getTime("ticketTiming"),
                        rs.getString("paymentId")
                );
                tickets.add(ticket);
            }
        }
        return tickets;
    }
}
