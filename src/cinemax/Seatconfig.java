package cinemax;

import java.sql.*;

public class Seatconfig {
    int configId;
    static int path;
    int classApercentage;
    int classBpercentage;
    int classCpercentage;
    int screenId;
    int cpath1;
    int cpath2;
    static int speaker;

    public Seatconfig(int path, int classApercentage, int classBpercentage, int classCpercentage, int screenId,int speaker) {
        //this.configId = configId;
        this.path = path;
        this.classApercentage = classApercentage;
        this.classBpercentage = classBpercentage;
        this.classCpercentage = classCpercentage;
        this.screenId = screenId;
        this.speaker=speaker;
    }
    public static void printseat(int id){
        String sql = "SELECT * FROM screen where screenId= ? ";

        Connection conn = null;
        int totalSeats=0;
        int rowSeats=10;
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            //ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                totalSeats = rs.getInt("seatCount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//    public static void (String[] args) {
//        int total=150;
//        int rseat=15;
//
//        int pathsize=3;
//        int classApercent=30;
//        int classBpercent=40;
//        int classCpercent=30;
//        int Aseat=((total*classApercent)/100)/ rseat;
//        int Bseat=((total*classBpercent)/100)/rseat;
//        int Cseat=((total*classCpercent)/100)/rseat;
//        int cpath1=3;
//        int cpath2=16;
//        print(row,rseat,pathsize,Aseat,Bseat,Cseat,cpath1,cpath2);
//    }
    public static void printff(int total,int rowseat,int path,int a1,int b1,int c1,int cpath1,int cpath2,int speaker){
        int p1=001;
        int s1=001;
        int n1=001;
        int row=total/rowseat;
        int col=rowseat;
        int a=((total*a1)/100)/rowseat;
        int b=((total*b1)/100)/rowseat;
        int c=((total*c1)/100)/rowseat;
        //boolean f=false;
        for(int i=0;i<row+(2*path)+3+4;i++){
            for(int j=0;j<col+(2*path);j++){
                if(speaker!=0 && i%speaker==0 &&(j==0 || j==col+(2*path)-1)){
                    System.out.print("L");
                }else if (speaker!=0 &&  i%speaker!=0 &&(j==0 || j==col+(2*path)-1)) {
                    System.out.print(" ");
                }
                if(i<a){
                    if((j>=cpath1 && j<cpath1+path) || (j>cpath2 && j<=cpath2+path)){
                        System.out.print("____ ");
                    }else{
                        if(p1<=9){
                            System.out.print("p00"+p1+" ");
                        }
                        else if(p1>9 && p1<=99){
                            System.out.print("p0"+p1+" ");
                        }else{
                            System.out.print("p"+p1+" ");
                        }
                        p1++;
                    }
                }
                else if(i>=a && i<a+path){
                    if(j==0 || j==col+(2*path)-1){
                        System.out.print("  D  ");
                    }else{
                        System.out.print("____ ");}
                }
                else if(i==a+path){
                    System.out.print("  X  ");
                }
                else if(i>a+path && i<=a+path+b){
                    if((j>=cpath1 && j<cpath1+path) || (j>cpath2 && j<=cpath2+path)){
                        System.out.print("____ ");
                    }else{
                        if(s1<=9){
                            System.out.print("S00"+s1+" ");
                        }
                        else if(s1>9 && s1<=99){
                            System.out.print("S0"+s1+" ");
                        }else{
                            System.out.print("S"+s1+" ");
                        }
                        s1++;
                    }
                }
                else if(i>=a+path+b+1 && i<=a+(2*path)+b){
                    if(j==0 || j==col+(2*path)-1){
                        System.out.print("  D  ");
                    }else{
                        System.out.print("____ ");}
                }
                else if(i>a+path+1+b && i<a+path+1+b+path){
                    System.out.print("____ ");
                }
                else if(i==row+(2*path)+3-c-2){
                    System.out.print("  X  ");
                }else if(i>a+path-1+path && i<row+(2*path)+2){
                    if((j>=cpath1 && j<cpath1+path) || (j>cpath2 && j<=cpath2+path)){
                        System.out.print("____ ");
                    }else{
                        if(n1<=9){
                            System.out.print("N00"+n1+" ");
                        }
                        else if(n1>9 && n1<=99){
                            System.out.print("N0"+n1+" ");
                        }else{
                            System.out.print("N"+n1+" ");
                        }
                        n1++;
                    }
                }else if(i>row+(2*path)+3-c-path && i<row+(2*path)+3-c){
                    System.out.print("____ ");
                }
                else if(i==row+(2*path)+3){
                    if(j==0 || j==col+(2*path)-1){
                        System.out.print(" X ");
                    }
                    System.out.print("     ");
                }else if(i>row+(2*path)+3){
                    if(j>=path+1 && j<col+(2*path)-path-2){
                        System.out.print("  *  ");}
                    else{
                        if(j==0 || j==col+(2*path)-1)
                        {
                            System.out.print(" X ");
                        }
                        if(i==row+(2*path)+3+3 || j<path &&  j>=col+(2*path)-path-2){

                        }
                        System.out.print("     ");
                    }
                }else{
                    System.out.print("  X  ");
                }
            }
            System.out.println();
        }
    }



    public static void print(int screenId, int path, int a1, int b1, int c1, int cpath1, int cpath2,int speaker) throws SQLException {
        String sql = "SELECT * FROM screen where id= ? ";

        Connection conn = DatabaseConnection.getConnection();
        int totalSeats=0;
        int rowSeats=10;
        try {
             PreparedStatement pstmt = conn.prepareStatement(sql);
             pstmt.setInt(1, screenId);
             ResultSet rs = pstmt.executeQuery();
             //ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                totalSeats = rs.getInt("seatCount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int p1 = 1;
        int s1 = 1;
        int n1 = 1;
       // String query="Select * from ";
        int row=totalSeats/rowSeats;
        int col=rowSeats;
        int a=((totalSeats*a1)/100)/rowSeats;
        int b=((totalSeats*b1)/100)/rowSeats;
        int c=((totalSeats*c1)/100)/rowSeats;
        for (int i = 0; i < row + (2 * path) + 3 + 4; i++) {
            for (int j = 0; j < col + (2 * path); j++) {
                if (i < a) {
                    if ((j >= cpath1 && j < cpath1 + path) || (j > cpath2 && j <= cpath2 + path)) {
                        System.out.print("____ ");
                    } else {
                        System.out.printf("p%03d ", p1++);
                    }
                } else if (i >= a && i < a + path) {
                    if (j == 0 || j == col + (2 * path) - 1) {
                        System.out.print("  D  ");
                    } else {
                        System.out.print("____ ");
                    }
                }if(speaker!=0 && i%speaker==0){
                    if(j==0 || j==col + (2 * path)-1){
                        System.out.print("L");
                    }
                }
                else if(i==row+(2*path)+3-c-2){
                    System.out.print("  X  ");}
                else if (i == a + path) {
                    System.out.print("  X  ");
                } else if (i > a + path && i <= a + path + b) {
                    if ((j >= cpath1 && j < cpath1 + path) || (j > cpath2 && j <= cpath2 + path)) {
                        System.out.print("____ ");
                    } else {
                        System.out.printf("S%03d ", s1++);
                    }
                } else if (i >= a + path + b + 1 && i <= a + (2 * path) + b) {
                    if (j == 0 || j == col + (2 * path) - 1) {
                        System.out.print("  D  ");
                    } else {
                        System.out.print("____ ");
                    }
                } else if (i > row + (2 * path) + 3 - c - 2 && i < row + (2 * path) + 2) {
                    if ((j >= cpath1 && j < cpath1 + path) || (j > cpath2 && j <= cpath2 + path)) {
                        System.out.print("____ ");
                    } else {
                        System.out.printf("N%03d ", n1++);
                    }
                } else if (i == row + (2 * path) + 3) {
                    if (j == 0 || j == col + (2 * path) - 1) {
                        System.out.print(" X ");
                    }
                    System.out.print("     ");
                } else if (i > row + (2 * path) + 3) {
                    if (j >= path + 1 && j < col + (2 * path) - path - 2) {
                        System.out.print("  *  ");
                    } else {
                        if (j == 0 || j == col + (2 * path) - 1) {
                            System.out.print(" X ");
                        }
                        System.out.print("     ");
                    }
                }
            }
            System.out.println();
        }
    }
    public static void retrieveAndPrintScreenLayout(Connection conn, int screenId) throws SQLException {
        String sql = "SELECT seatCount, rowcount FROM screen WHERE id = ?";
        int totalSeats = 1;
        int rowcount = 1;
        int path = 1;
        int a = 1;
        int b = 1;
        int c = 1;
        int path1 = 1;
        int path2 = 1;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, screenId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalSeats = rs.getInt("seatCount");
                    rowcount = rs.getInt("rowcount");
                } else {
                    System.out.println("No screen found with ID: " + screenId);
                    return;
                }
            }

            String query = "SELECT path, classApercentage, classBpercentage, classCpercentage, path1start, path2start FROM screen_config WHERE screenId = ?";
            try (PreparedStatement psmt = conn.prepareStatement(query)) {
                psmt.setInt(1, screenId);

                try (ResultSet rs = psmt.executeQuery()) {
                    if (rs.next()) {
                        path = rs.getInt("path");
                        a = rs.getInt("classApercentage");
                        b = rs.getInt("classBpercentage");
                        c = rs.getInt("classCpercentage");
                        path1 = rs.getInt("path1start");
                        path2 = rs.getInt("path2start");
                    }
                }

                int row = (rowcount != 0) ? totalSeats / rowcount : 0;
                int col = rowcount;

                if (row == 0 || col == 0) {
                    System.out.println("Invalid layout configuration: rowcount is zero.");
                    return;
                }

                print(screenId, path, a, b, c, path1, path2, speaker);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to insert data into the database using parameters
    public void insertIntoDatabase( int screenId,int pathid, int classApercentage, int classBpercentage, int classCpercentage,int speaker,int path1,int path2) {
        String url = "jdbc:mysql://127.0.0.1:3306/cinemax";
        String user = "root";
        String password = "BENstokes@55";

        String query = "UPDATE screen_config SET path = ?, classApercentage = ?, classBpercentage = ?, classCpercentage = ?, screenId = ?, speaker = ? ,path1start=?,path2start=? WHERE ScreenId=?";

        try (Connection conna = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conna.prepareStatement(query)) {

            // Set the parameters for the prepared statement

            pstmt.setInt(1, pathid);
            pstmt.setInt(2, classApercentage);
            pstmt.setInt(3, classBpercentage);
            pstmt.setInt(4, classCpercentage);
            pstmt.setInt(5, screenId);
            pstmt.setInt(6,speaker);
            pstmt.setInt(7,path1);
            pstmt.setInt(8,path2);
            pstmt.setInt(9,screenId);
            // Execute the insert operation
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


