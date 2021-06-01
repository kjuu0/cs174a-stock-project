package backend;

public class Transaction {
    protected int id;
    protected int taxId;
    protected String date;
    protected long timestamp;
    
    public Transaction(int trId, int taId, String d, long t) {
        id = trId; 
        taxId = taId;
        date = d;
        timestamp = t;
    }
    
    public long getTimestamp() {
        return timestamp; 
    }

}
