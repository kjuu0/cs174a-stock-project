package backend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StockAccountManager {
    Connection conn; 
    
    public StockAccountManager(Connection c) {
        conn = c; 
    }
    
    public int getSharesOwnedAtPrice(final int taxid, final String stockSymbol, final int pricePerShare) {
        final String QUERY = "SELECT shares FROM Owns_Stock WHERE tax_id=" + taxid + " AND stock_symbol=\"" + stockSymbol + "\" AND price_per_share=" + pricePerShare; 
        
        try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(QUERY);
            
            if (rs.next()) {
                return rs.getInt("shares") ;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return 0;
    }
}
