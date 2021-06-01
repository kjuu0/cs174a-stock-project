package backend;

public class MovieData {
    private String title;
    private int year;
    private String genre;
    private int rating;
    private int revenue;

    public MovieData(String t, int y, String g, int ra, int re) {
        title = t;
        year = y;
        genre = g;
        rating = ra;
        revenue = re;
    }
    
    public String toString() {
        return String.format("Title: %s\nYear: %d\nGenre: %s\nRating (/10): %d.%d\nRevenue ($Millions): %d", title, year, genre, rating / 10, rating % 10, revenue);
    }
}
