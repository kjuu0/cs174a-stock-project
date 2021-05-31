package backend;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MarketAccountManager {
    private Connection conn;

    public MarketAccountManager(Connection c) {
        conn = c; 
    }
    
    public int getBalance(int taxid) {
        final String QUERY = "SELECT balance FROM Market_Account WHERE tax_id=" + taxid; 
        try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(QUERY);
           
            if (rs.next()) {
                return rs.getInt("balance");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return -1;
    }
}
