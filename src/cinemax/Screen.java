package cinemax;

import cinemax.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Screen {
    int screenId;
    String screenName;
    int totalSeats;
    String screenType;

    public Screen(int screenId, String screenName, int totalSeats, String screenType) {
        this.screenId = screenId;
        this.screenName = screenName;
        this.totalSeats = totalSeats;
        this.screenType = screenType;
    }

    public static void save(Screen screen) throws SQLException {
        String query = "INSERT INTO Screen (screenId, screenName, totalSeats, screenType) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, screen.screenId);
            stmt.setString(2, screen.screenName);
            stmt.setInt(3, screen.totalSeats);
            stmt.setString(4, screen.screenType);
            stmt.executeUpdate();
        }
    }

    public static List<Screen> getAll() throws SQLException {
        List<Screen> screens = new ArrayList<>();
        String query = "SELECT * FROM Screen";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Screen screen = new Screen(
                        rs.getInt("screenId"),
                        rs.getString("screenName"),
                        rs.getInt("totalSeats"),
                        rs.getString("screenType")
                );
                screens.add(screen);
            }
        }
        return screens;
    }
}
