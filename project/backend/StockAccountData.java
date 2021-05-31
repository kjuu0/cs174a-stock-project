package backend;

public class StockAccountData {
    private String symbol;
    private int price;
    private int shares; 
    
    public StockAccountData(String s, int p, int sh) {
        symbol = s;
        price = p;
        shares = sh;
    }
    
    public String toString() {
        int pInt = price / 100, pDec = price % 100;
        return String.format("%s: %d x $%d.%02d", symbol, shares, pInt, pDec); 
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public int getPrice() {
        return price;
    }
    
    public int getShares() {
        return shares;
    }
}
