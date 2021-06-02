package backend;

public class MarketAccount {
    private int taxid;
    private long balance;

    public MarketAccount(int id, long b){
        taxid = id;
        balance = b;
    }

    public int getTaxID() {
        return taxid;
    }

    public long getBalance() {
        return balance;
    }
}
