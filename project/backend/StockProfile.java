package backend;

public class StockProfile {
    private Stock stockInfo;
    private String name;
    private String birthday; 
    
    public StockProfile(String symbol, String date, int price, String name, String bday) {
        stockInfo = new Stock(symbol, date, price); 
        this.name = name;
        this.birthday = bday;
    }
    
    public String toString() {
        int pInt = stockInfo.getPrice() / 100, pDec = stockInfo.getPrice() % 100;
        return String.format("%s (%s): %s | $%d.%02d per share", name, stockInfo.getSymbol(), birthday, pInt, pDec);
    }
}
