package backend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}
