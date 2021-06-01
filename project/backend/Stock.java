package backend;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Stock {
    private String symbol;
    private String date;
    private int price; 
  
    public Stock(String s, String d, int p) {
        symbol = s;
        date = d;
        price = p; 
    }

    public Stock(ResultSet rs) {
        try {
            symbol = rs.getString("stock_symbol");
            date = rs.getString("date");
            price = rs.getInt("price");
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
    }
    
    public String getSymbol() {
        return symbol; 
    }
    
    public int getPrice() {
        return price; 
    }
    
    public String getDate() {
        return date; 
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
    
    public String toString() {
        int pInt = price / 100, pDec = price % 100;
        return String.format("%s: $%d.%02d", symbol, pInt, pDec);
    }
}
