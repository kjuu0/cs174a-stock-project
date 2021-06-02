package backend;

public class AccrueInterestTransaction extends Transaction{
    private int amount;
    private float rate;
    public AccrueInterestTransaction(int id, String date, long timestamp, int tId, int a, float r) {
        super(id, tId, date, timestamp);
        this.amount = a;
        this.rate = r;
    }

    public int getAmount() {
        return this.amount;
    }

    public float getRate() {
        return this.rate;
    }

    public String toString() {
        return String.format("%s - INTEREST %.2f%% -> $%d.%02d", this.date, this.rate, this.amount / 100, this.amount % 100);
    }
    
}
