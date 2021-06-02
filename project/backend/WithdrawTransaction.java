package backend;

public class WithdrawTransaction extends Transaction{
    private int amount;
    public WithdrawTransaction(int id, String date, long timestamp, int tId, int a) {
        super(id, tId, date, timestamp);
        this.amount = a;
    }

    public int getAmount() {
        return amount;
    }

    public String toString() {
        return String.format("%s - WITHDRAW $%d.%02d", this.date, this.amount / 100, this.amount % 100);
    }
}
