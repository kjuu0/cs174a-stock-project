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
    
    public List<SellTransaction> getSellTransactions(final int taxid) {
        List<SellTransaction> transactions = new ArrayList<>();
        final String QUERY = "SELECT * FROM Sell WHERE tax_id=" + taxid + " ORDER BY timestamp DESC";

        try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(QUERY);
           
            while (rs.next()) {
                transactions.add(new SellTransaction(
                    rs.getInt("transaction_id"),
                    rs.getString("transaction_date"),
                    rs.getLong("timestamp"),
                    rs.getInt("tax_id"),
                    rs.getString("stock_symbol"),
                    rs.getInt("shares"),
                    rs.getInt("price_per_share_bought"),
                    rs.getInt("price_per_share_sold")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return transactions;
    } 
    
    public List<BuyTransaction> getBuyTransactions(final int taxid) {
        List<BuyTransaction> transactions = new ArrayList<>();
        final String QUERY = "SELECT * FROM Buy WHERE tax_id=" + taxid + " ORDER BY timestamp DESC";

        try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(QUERY);
           
            while (rs.next()) {
                transactions.add(new BuyTransaction(
                    rs.getInt("transaction_id"),
                    rs.getString("transaction_date"),
                    rs.getLong("timestamp"),
                    rs.getInt("tax_id"),
                    rs.getString("stock_symbol"),
                    rs.getInt("shares"),
                    rs.getInt("price_per_share")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return transactions;
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
