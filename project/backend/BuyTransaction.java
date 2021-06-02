package backend;

public class BuyTransaction extends Transaction{
    private Stock stock;
    private int shares;
    
    public BuyTransaction(int id, String d, long timestamp, int taxId, String symbol, int shares, int price) {
        super(id, taxId, d, timestamp); 
        stock = new Stock(symbol, d, price);
        this.shares = shares;
    }

    public Stock getStock() {
           return this.stock;
    }

    public int getShares() {
           return this.shares;
    }
    
    public String toString() {
        // Maybe create custom currency class to avoid this calculation every time?
        int pInt = stock.getPrice() / 100, pDec = stock.getPrice() % 100;
        return String.format("%s - BUY %dx %s $%d.%02d/share", this.date, this.shares, this.stock.getSymbol(), pInt, pDec);
    }
}
