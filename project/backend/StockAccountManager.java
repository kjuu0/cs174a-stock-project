package backend;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StockAccountManager {
    Connection conn;
    SysManager sysManager;
    
    public StockAccountManager(Connection c, SysManager sm) {
        conn = c;
        sysManager = sm;
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

    public List<SellTransaction> getSellTransactionsThisMonth(final int taxid) {
        String year = sysManager.getYear();
        String month = sysManager.getMonth();
        List<SellTransaction> sells = new ArrayList<>();
        final String QUERY = "SELECT * FROM Sell WHERE tax_id=" + taxid
            + " AND transaction_date LIKE \"" + year + "/" + month + "/" + "%\""
            + " ORDER BY timestamp DESC";

        try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(QUERY);
           
            while (rs.next()) {
                sells.add(new SellTransaction(
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
        
        return sells;
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

    public List<BuyTransaction> getBuyTransactionsThisMonth(final int taxid) {
        String year = sysManager.getYear();
        String month = sysManager.getMonth();
        List<BuyTransaction> buys = new ArrayList<>();
        final String QUERY = "SELECT * FROM Buy WHERE tax_id=" + taxid
            + " AND transaction_date LIKE \"" + year + "/" + month + "/" + "%\""
            + " ORDER BY timestamp DESC";

        try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(QUERY);
           
            while (rs.next()) {
                buys.add(new BuyTransaction(
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
        
        return buys;
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
        final String QUERY = "SELECT stock_symbol, shares, price_per_share FROM Owns_Stock WHERE tax_id=" + taxid + " ORDER BY stock_symbol, price_per_share"; 
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
    
    public List<StockAccountData> getOwnedStocks(int taxid) {
        List<StockAccountData> data = new ArrayList<>();
        final String QUERY = "SELECT stock_symbol, SUM(shares) AS total_shares FROM Owns_Stock WHERE tax_id=" + taxid + " GROUP BY stock_symbol"; 
        try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(QUERY);
            while (rs.next()) {
                // We don't care about price per share here
                data.add(new StockAccountData(rs.getString("stock_symbol"), -1, rs.getInt("total_shares"))); 
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        return data;
    }
    
    public int getStockAccountBalance(int taxid) {
        final String QUERY = "SELECT SUM(price * total_shares) as balance " 
            + "FROM (SELECT stock_symbol, SUM(shares) as total_shares FROM Owns_Stock WHERE tax_id=" + taxid + " GROUP BY stock_symbol)" 
            + "NATURAL JOIN (SELECT stock_symbol, price FROM Stock WHERE date=\"" + sysManager.getDate() + "\")";

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
