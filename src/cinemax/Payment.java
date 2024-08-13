package cinemax;

import cinemax.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Payment {
    String paymentId;
    int ticketId;
    double amount;
    String paymentMethod;
    Date paymentDate;
    String paymentStatus;
    String transactionId;
    String payerName;
    String payerEmail;

    public Payment(String paymentId, int ticketId, double amount, String paymentMethod, Date paymentDate, String paymentStatus, String transactionId, String payerName, String payerEmail) {
        this.paymentId = paymentId;
        this.ticketId = ticketId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
        this.payerName = payerName;
        this.payerEmail = payerEmail;
    }

    public static void save(Payment payment) throws SQLException {
        String query = "INSERT INTO Payment (paymentId, ticketId, amount, paymentMethod, paymentDate, paymentStatus, transactionId, payerName, payerEmail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, payment.paymentId);
            stmt.setInt(2, payment.ticketId);
            stmt.setDouble(3, payment.amount);
            stmt.setString(4, payment.paymentMethod);
            stmt.setDate(5, payment.paymentDate);
            stmt.setString(6, payment.paymentStatus);
            stmt.setString(7, payment.transactionId);
            stmt.setString(8, payment.payerName);
            stmt.setString(9, payment.payerEmail);
            stmt.executeUpdate();
        }
    }

    public static List<Payment> getAll() throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM Payment";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Payment payment = new Payment(
                        rs.getString("paymentId"),
                        rs.getInt("ticketId"),
                        rs.getDouble("amount"),
                        rs.getString("paymentMethod"),
                        rs.getDate("paymentDate"),
                        rs.getString("paymentStatus"),
                        rs.getString("transactionId"),
                        rs.getString("payerName"),
                        rs.getString("payerEmail")
                );
                payments.add(payment);
            }
        }
        return payments;
    }
}
