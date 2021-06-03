package backend;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class StockManager {
    private Connection conn;
    
    public StockManager(Connection c) {
        conn = c; 
    }
    
    public boolean setStockPrice(final String symbol, final int value, final String date) {
        final String UPDATE = "UPDATE Stock SET price=" + value + " WHERE stock_symbol=\"" + symbol + "\" AND date=\"" + date + "\"";

        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(UPDATE);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return false;
    }
    
    public List<Stock> getStocksForDate(final String date) {
        List<Stock> stocks = new ArrayList<>();
        
        final String QUERY = "SELECT * FROM Stock WHERE date=\"" + date + "\"";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
            while (rs.next()) {
                stocks.add(new Stock(rs)); 
            } 
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return stocks;
    }
    
    public List<StockProfile> getStockProfilesForDate(final String date) {
        
     List<StockProfile> profiles = new ArrayList<>();
        
        final String QUERY = "SELECT * FROM (SELECT * FROM Stock WHERE date=\"" + date + "\") NATURAL JOIN Stock_Profile";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
            while (rs.next()) {
                final String CONTRACT_QUERY = "SELECT * FROM Movie_Contract WHERE symbol=\"" + rs.getString("stock_symbol") + "\"";
                StockProfile sp = new StockProfile(rs.getString("stock_symbol"),
                    rs.getString("date"),
                    rs.getInt("price"),
                    rs.getString("name"),
                    rs.getString("birthdate"));

                Statement s = conn.createStatement();
                ResultSet cs = s.executeQuery(CONTRACT_QUERY);
                while (cs.next()) {
                    sp.addContract(new MovieContract(cs.getString("movie_title"),
                        cs.getString("symbol"),
                        cs.getInt("year"),
                        cs.getInt("total"),
                        cs.getString("role")));
                }
                
                profiles.add(sp);
            } 
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return profiles;
    }
    
    public int getStockPriceOnDate(final String stockSymbol, final String date) {
        final String QUERY = "SELECT price FROM Stock WHERE stock_symbol=\"" + stockSymbol + "\" AND date=\"" + date + "\"";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
            if (rs.next()) {
                return rs.getInt("price"); 
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }

        return -1;
    }
}
