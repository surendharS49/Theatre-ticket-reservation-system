package cinemax;

import cinemax.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Seat {
    int seatId;
    int seatNumber;
    boolean booked;
    int screenId;
    String seatType;

    public Seat(int seatId, int seatNumber, boolean booked, int screenId, String seatType) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.booked = booked;
        this.screenId = screenId;
        this.seatType = seatType;
    }

    public static void save(Seat seat) throws SQLException {
        String query = "INSERT INTO Seat (seatId, seatNumber, booked, screenId, seatType) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, seat.seatId);
            stmt.setInt(2, seat.seatNumber);
            stmt.setBoolean(3, seat.booked);
            stmt.setInt(4, seat.screenId);
            stmt.setString(5, seat.seatType);
            stmt.executeUpdate();
        }
    }

    public static List<Seat> getAll() throws SQLException {
        List<Seat> seats = new ArrayList<>();
        String query = "SELECT * FROM Seat";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Seat seat = new Seat(
                        rs.getInt("seatId"),
                        rs.getInt("seatNumber"),
                        rs.getBoolean("booked"),
                        rs.getInt("screenId"),
                        rs.getString("seatType")
                );
                seats.add(seat);
            }
        }
        return seats;
    }
}
