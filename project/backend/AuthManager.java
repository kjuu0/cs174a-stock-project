package backend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthManager {
    private Connection conn;

    public AuthManager(Connection c) {
        this.conn = c;
    }

    public Customer authenticateTrader(String username, String password) {
        final String QUERY = "SELECT * FROM Customer WHERE username=\"" + username +
        "\" AND password=\"" + password + "\"";
        
        try {
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);

            if (!rs.next()) {
                System.out.println("Did not find user"); 
                return null;
            } else {
                return new Customer(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }

        return null;
    }

    public boolean isManager(int taxid) {
        final String QUERY = "SELECT * FROM Manager WHERE tax_id=" + taxid;

        try {
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);

            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }

        return true;
    }

}
