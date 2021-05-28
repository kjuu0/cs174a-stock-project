package backend;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Stock {
    private String symbol;
    private String date;
    private int price; 
    
    public static Stock constructStock(ResultSet rs) {
        Stock s = new Stock(); 
        try {
            s.setSymbol(rs.getString("stock_symbol"));
            s.setDate(rs.getString("date"));
            s.setPrice(rs.getInt("price"));
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
       
        return s;
    }
    
    public void setSymbol(String s) {
        symbol = s;
    }
    
    public void setDate(String d) {
        date = d; 
    }
    
    public void setPrice(int p) {
        price = p; 
    }
}
