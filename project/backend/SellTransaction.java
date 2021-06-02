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

     public Stock getStockSold() {
            return this.stockSold;
     }

     public int getShares() {
            return this.shares;
     }

     public int getPriceSold() {
            return this.priceSold;
     }

     public int getNetDifference() {
            return (this.priceSold - stockSold.getPrice()) * shares;
     }
     
     public String toString() {
            int pbInt = stockSold.getPrice() / 100, pbDec = stockSold.getPrice() % 100; 
            int psInt = priceSold / 100, psDec = priceSold % 100;
            return String.format("%s - SELL %dx %s $%d.%02d/share at $%d.%02d/share", this.date, this.shares, stockSold.getSymbol(), pbInt, pbDec, psInt, psDec);
     }
}
