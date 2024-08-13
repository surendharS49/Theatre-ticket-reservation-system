package cinemax;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TheatreBranch {
    int branchId;
    String branchName;
    String location;
    int totalScreens;

    public TheatreBranch(int branchId, String branchName, String location, int totalScreens) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.location = location;
        this.totalScreens = totalScreens;
    }
    public static void save(TheatreBranch branch) throws SQLException {
        String query = "INSERT INTO TheatreBranch (branchId, branchName, location, totalScreens) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, branch.branchId);
            stmt.setString(2, branch.branchName);
            stmt.setString(3, branch.location);
            stmt.setInt(4, branch.totalScreens);
            stmt.executeUpdate();
        }
    }
    public static List<TheatreBranch> getAll() throws SQLException {
        List<TheatreBranch> branches = new ArrayList<>();
        String query = "SELECT * FROM TheatreBranch";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                TheatreBranch branch = new TheatreBranch(
                        rs.getInt("branchId"),
                        rs.getString("branchName"),
                        rs.getString("location"),
                        rs.getInt("totalScreens")
                );
                branches.add(branch);
            }
        }
        return branches;
    }
    public static void update(TheatreBranch branch) throws SQLException {
        String query = "UPDATE TheatreBranch SET branchName = ?, location = ?, totalScreens = ? WHERE branchId = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, branch.branchName);
            stmt.setString(2, branch.location);
            stmt.setInt(3, branch.totalScreens);
            stmt.setInt(4, branch.branchId);
            stmt.executeUpdate();
        }
    }
    public static void delete(int branchId) throws SQLException {
        String query = "DELETE FROM TheatreBranch WHERE branchId = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, branchId);
            stmt.executeUpdate();
        }
    }
    public static TheatreBranch getById(int branchId) throws SQLException {
        String query = "SELECT * FROM TheatreBranch WHERE branchId = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, branchId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new TheatreBranch(
                        rs.getInt("branchId"),
                        rs.getString("branchName"),
                        rs.getString("location"),
                        rs.getInt("totalScreens")
                );
            }
        }
        return null;
    }
    public static void printAll() throws SQLException {
        List<TheatreBranch> branches = getAll();
        System.out.println("------ THEATRE BRANCHES ------");
        for (TheatreBranch branch : branches) {
            System.out.println("Branch ID: " + branch.branchId);
            System.out.println("Branch Name: " + branch.branchName);
            System.out.println("Location: " + branch.location);
            System.out.println("Total Screens: " + branch.totalScreens);
            System.out.println("---------------");
        }
        System.out.println("------------------------------");
    }
}
