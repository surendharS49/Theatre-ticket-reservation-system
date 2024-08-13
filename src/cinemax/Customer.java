package cinemax;

import cinemax.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    int userId;
    String name;
    String email;
    long phone;
    String address;
    String membershipLevel;
    String preferredLanguage;
    String password;

    public Customer(int userId, String name, String email, long phone, String address, String membershipLevel, String preferredLanguage, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.membershipLevel = membershipLevel;
        this.preferredLanguage = preferredLanguage;
        this.password = password;
    }

    public static void save(Customer customer) throws SQLException {
        String query = "INSERT INTO Customer (userId, name, email, phone, address, membershipLevel, preferredLanguage, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customer.userId);
            stmt.setString(2, customer.name);
            stmt.setString(3, customer.email);
            stmt.setLong(4, customer.phone);
            stmt.setString(5, customer.address);
            stmt.setString(6, customer.membershipLevel);
            stmt.setString(7, customer.preferredLanguage);
            stmt.setString(8, customer.password);
            stmt.executeUpdate();
        }
    }

    public static List<Customer> getAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM Customer";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("userId"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getLong("phone"),
                        rs.getString("address"),
                        rs.getString("membershipLevel"),
                        rs.getString("preferredLanguage"),
                        rs.getString("password")
                );
                customers.add(customer);
            }
        }
        return customers;
    }
}
