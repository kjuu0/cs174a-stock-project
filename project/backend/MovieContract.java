package backend;

public class MovieContract {
    private String movieTitle;
    private String stockSymbol;
    private int year;
    private int total;
    private String role;
    
    public MovieContract(String t, String s, int y, int tot, String r) {
        movieTitle = t;
        stockSymbol = s;
        year = y;
        total = tot;
        role = r; 
    }
    
    public String toString() {
        int tInt = total / 100, tDec = total % 100;
        return String.format("%s (%d) | %s | $%d.%02d", movieTitle, year, role, tInt, tDec);
    }
}
