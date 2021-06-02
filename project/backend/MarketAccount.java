package backend;

public class MarketAccount {
    private int taxid;
    private int balance;

    public MarketAccount(int id, int b){
        taxid = id;
        balance = b;
    }

    public int getTaxID() {
        return taxid;
    }

    public int getBalance() {
        return balance;
    }
}
