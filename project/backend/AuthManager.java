package backend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthManager {
    private Connection conn;
    private boolean isAuthenticated;
    private boolean isAdmin;

    public AuthManager(Connection c) {
        this.conn = c;
    }

    public boolean authenticateTrader(String username, String password) {
        final String QUERY = "SELECT username, password FROM Customer WHERE username=\"" + username +
        "\" AND password=\"" + password + "\"";
        
        try {
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);

            if (!rs.next()) {
                System.out.println("Did not find user"); 
                return false;
            } else {
                System.out.println("Found user with username " + username);
                this.isAuthenticated = true;
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }

        return false;
    }

}