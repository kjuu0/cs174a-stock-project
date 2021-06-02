package backend;

import java.util.ArrayList;
import java.util.List;

public class StockProfile {
    private Stock stockInfo;
    private String name;
    private String birthday; 
    private List<MovieContract> contracts;
    
    public StockProfile(String symbol, String date, int price, String name, String bday) {
        stockInfo = new Stock(symbol, date, price); 
        this.name = name;
        this.birthday = bday;
        contracts = new ArrayList<>();
    }
    
    public void addContract(MovieContract c) {
        contracts.add(c);
    }
    
    public String toString() {
        int pInt = stockInfo.getPrice() / 100, pDec = stockInfo.getPrice() % 100;
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s (%s): %s | $%d.%02d per share\n", name, stockInfo.getSymbol(), birthday, pInt, pDec));
        sb.append("Contracts:\n");
        for (MovieContract m : contracts) {
            sb.append(m.toString() + "\n");
        }
        
        return sb.toString();
    }
}
