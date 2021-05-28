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
    
    public List<Stock> getStocksForDate(String date) {
        List<Stock> stocks = new ArrayList<>();
        
        final String QUERY = "SELECT * FROM Stock WHERE date=\"" + date + "\"";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
            while (rs.next()) {
                stocks.add(Stock.constructStock(rs)); 
            } 
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return stocks;
    }
}
