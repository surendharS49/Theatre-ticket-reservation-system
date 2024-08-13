package cinemax;

import cinemax.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Movie {
    int id;
    String name;
    String type;
    int length;
    String language;
    String cast;

    public Movie(int id, String name, String type, int length, String language, String cast) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.length = length;
        this.language = language;
        this.cast = cast;
    }

    public static void save(Movie movie) throws SQLException {
        String query = "INSERT INTO Movie (id, name, type, length, language, cast) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movie.id);
            stmt.setString(2, movie.name);
            stmt.setString(3, movie.type);
            stmt.setInt(4, movie.length);
            stmt.setString(5, movie.language);
            stmt.setString(6, movie.cast);
            stmt.executeUpdate();
        }
    }

    public static List<Movie> getAll() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM Movie";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getInt("length"),
                        rs.getString("language"),
                        rs.getString("cast")
                );
                movies.add(movie);
            }
        }
        return movies;
    }
}
