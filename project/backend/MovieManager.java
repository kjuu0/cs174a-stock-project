package backend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class MovieManager {
    private Connection conn;
    
    public MovieManager(Connection c) {
        conn = c;
    }
    
    public MovieData getMovieData(String movieName) {
        final String QUERY = "SELECT * FROM Movie WHERE title=\"" + movieName + "\"";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY); 
            if (rs.next()) {
                return new MovieData(movieName, rs.getInt("year"), rs.getString("genre"), rs.getInt("rating"), rs.getInt("revenue")); 
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return null;
    }
    
    public List<String> getTopMoviesInRange(final int rating, final int start, final int end) {
        List<String> topNames = new ArrayList<>();
        final String QUERY = "SELECT title FROM Movie WHERE rating > " + rating + " AND year >= " + start + " AND year <= " + end;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY); 
            while (rs.next()) {
                topNames.add(rs.getString("title"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return topNames;
    }
}
