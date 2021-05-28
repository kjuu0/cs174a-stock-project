package backend;

import java.util.List;
import java.util.ArrayList;

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
    
    public List<StockAccountData> getStockAccountData(int taxid) {
        List<StockAccountData> data = new ArrayList<>();
        final String QUERY = "SELECT stock_symbol, shares, price_per_share FROM Owns_Stock WHERE tax_id=" + taxid + " GROUP BY stock_symbol ORDER BY price_per_share"; 
        try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(QUERY);
            while (rs.next()) {
                data.add(new StockAccountData(rs.getString("stock_symbol"), rs.getInt("price_per_share"), rs.getInt("shares"))); 
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        return data;
    }
}
