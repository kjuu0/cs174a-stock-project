package backend;

public class SellTransaction extends Transaction {
     private Stock stockSold;
     private int priceSold;
     private int shares;
     
     public SellTransaction(int id, String date, long timestamp, int tId, String symbol, int shares, int priceBought, int priceSold) {
            super(id, tId, date, timestamp); 
            stockSold = new Stock(symbol, date, priceBought);
            this.shares = shares;
            this.priceSold = priceSold;
     }
     
     public String toString() {
            int pbInt = stockSold.getPrice() / 100, pbDec = stockSold.getPrice() % 100; 
            int psInt = priceSold / 100, psDec = priceSold % 100;
            return String.format("SELL: %s | %s | %dx | $%d.%02d -> $%d.%02d", this.date, stockSold.getSymbol(), shares, pbInt, pbDec, psInt, psDec);
     }
}
