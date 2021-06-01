package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Controller {
    private Connection conn;
    private AuthManager authManager;
    private UserManager userManager;
    private SysManager sysManager;
    private StockManager stockManager;
    private MarketAccountManager maManager;
    private StockAccountManager saManager;
    private MovieManager mManager;
    private boolean isLoggedIn;
    private Customer user;

    public Controller() {
        isLoggedIn = false;

        this.conn = null;
        this.user = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:/home/kjuu/classes/cmpsc174a/cs174a-stock-project/project/db/datastore.db";
            // create a connection to the database
            this.conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            this.authManager = new AuthManager(this.conn);
            this.userManager = new UserManager(this.conn);
            this.sysManager = new SysManager(this.conn);
            this.stockManager = new StockManager(this.conn);
            this.maManager = new MarketAccountManager(this.conn);
            this.saManager = new StockAccountManager(this.conn);
            this.mManager = new MovieManager(this.conn);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public List<BuyTransaction> getStockBuyTransactions() {
        if (!isLoggedIn) {
            System.out.println("Cannot get transaction info if you're not logged in"); 
            return new ArrayList<>();
        } 
        
        return saManager.getBuyTransactions(user.taxid);
    }
    
    public List<SellTransaction> getStockSellTransactions() {
        if (!isLoggedIn) {
            System.out.println("Cannot get transaction info if you're not logged in"); 
            return new ArrayList<>();
        } 
        
        return saManager.getSellTransactions(user.taxid);
    }
    public boolean authenticateTrader(String username, String password) {
        user = authManager.authenticateTrader(username, password);
        if (user != null) {
            System.out.println("Welcome " + user.name);
            isLoggedIn = true;
        } else {
            System.out.println("Invalid user"); 
        }
        
        return user != null;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean logout() {
        if (isLoggedIn) {
            isLoggedIn = false;
            user = null;
            return true;
        }
        
        return false;
    }
    
    public List<StockAccountData> getStockAccountData() {
        if (!isLoggedIn) {
            System.out.println("Must be logged in to get stock account data");
            return new ArrayList<StockAccountData>();
        }
       
        return saManager.getStockAccountData(user.taxid);
    }
    
    public int getBalance() {
        if (!isLoggedIn) {
            System.out.println("Must be logged in to get market account balance");
            return -1;
        } 
        
        return maManager.getBalance(user.taxid);
    }
    
    public boolean sell(StockAccountData data, int shares) {
        if (shares < 0) {
            System.out.println("Cannot sell a negative nubmer of stocks");
            return false;
        }
        if (shares > data.getShares()) {
            System.out.println("You cannot sell more stocks than you own");
            return false;
        }

        final String date = sysManager.getDate();
        final int sellPrice = stockManager.getStockPriceOnDate(data.getSymbol(), date);

        final int profit = sellPrice * shares - 2000; // $20.00 commission fee

        final String UPDATE_SELL = "INSERT INTO Sell"
            + "(transaction_date, timestamp, tax_id, stock_symbol, shares, price_per_share_bought, price_per_share_sold) "
            + "VALUES (\"" + date + "\", " + System.currentTimeMillis() / 1000 + ", " + user.taxid + ", \"" + data.getSymbol() + "\", " + shares + ", " + data.getPrice() + ", " + sellPrice + ")"; 

        final String UPDATE_MARKET = "UPDATE Market_Account SET balance = balance + " + profit + " WHERE tax_id = " + user.taxid;
        String UPDATE_OWNS;
        
        if (shares == data.getShares()) {
            UPDATE_OWNS = "DELETE FROM Owns_Stock WHERE tax_id=" + user.taxid + " AND stock_symbol=\"" + data.getSymbol() + "\" AND price_per_share=" + data.getPrice();
        } else {
            UPDATE_OWNS = "UPDATE Owns_Stock SET shares=" + (data.getShares() - shares) + " WHERE tax_id=" + user.taxid + " AND stock_symbol=\"" + data.getSymbol() + "\" AND price_per_share=" + data.getPrice();
        }

        try {
            Statement stmt = conn.createStatement();
            stmt.addBatch(UPDATE_SELL);
            stmt.addBatch(UPDATE_MARKET);
            stmt.addBatch(UPDATE_OWNS);
            stmt.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
            return false;
        }

        System.out.println(String.format("Transaction success: sold %dx %s bought at $%d.%02d per share at $%d.%02d per share, total transaction profit of $%d.%02d including the $20.00 commission fee",
            shares, data.getSymbol(), data.getPrice() / 100, data.getPrice() % 100, sellPrice / 100, sellPrice % 100, profit / 100, profit % 100));
        return true;
    }
    
    public boolean purchase(Stock stock, int shares) {
        if (shares < 0) {
            System.out.println("Cannot purchase a negative number of stocks");
            return false;
        }
        final int totalPrice = stock.getPrice() * shares + 2000; // $20 commission fee
        final int customerBalance = maManager.getBalance(user.taxid);
        final int finalBalance = customerBalance - totalPrice;
        
        if (finalBalance < 0) {
            System.out.println("User has insufficient funds to make the purchase"); 
            return false;
        }

        final String UPDATE_BUY = "INSERT INTO Buy"
            + "(transaction_date, timestamp, tax_id, stock_symbol, shares, price_per_share) "
            + "VALUES (\"" + stock.getDate() + "\", " + System.currentTimeMillis() / 1000 + ", " + user.taxid + ", \"" + stock.getSymbol() + "\", " + shares + ", " + stock.getPrice() + ")";
        final String UPDATE_MARKET = "UPDATE Market_Account SET balance = " + finalBalance + " WHERE tax_id = " + user.taxid;
        
        String UPDATE_OWNS;
        final int sharesOwned = saManager.getSharesOwnedAtPrice(user.taxid, stock.getSymbol(), stock.getPrice());
       
        if (sharesOwned == 0) {
            UPDATE_OWNS = "INSERT INTO Owns_Stock VALUES (" + user.taxid + ", \"" + stock.getSymbol() + "\", " + shares + ", " + stock.getPrice() + ")";
        } else {
            UPDATE_OWNS = "UPDATE Owns_Stock SET shares=" + (sharesOwned + shares) + " WHERE tax_id=" + user.taxid + " AND stock_symbol=\"" + stock.getSymbol() + "\" AND price_per_share=" + stock.getPrice(); 
        }

        try {
            
            Statement stmt = conn.createStatement();
            stmt.addBatch(UPDATE_BUY);
            stmt.addBatch(UPDATE_MARKET);
            stmt.addBatch(UPDATE_OWNS);
            stmt.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
            return false;
        }
        System.out.println(String.format("Transaction success: bought %dx %s at $%d.%02d per share, total transaction cost of $%d.%02d including the $20.00 commission fee",
            shares, stock.getSymbol(), stock.getPrice() / 100, stock.getPrice() % 100, totalPrice / 100, totalPrice % 100));
        return true;
    }

    public boolean createTrader(Customer c) {
        boolean res = this.userManager.createTrader(c);
        if (res) {
            isLoggedIn = true;
            user = c;
        }
        return res;
    }
    
    public boolean isMarketOpen() {
        return sysManager.isMarketOpen(); 
    }
    
    public String getDate() {
        return sysManager.getDate(); 
    }
    
    public List<Stock> getAvailableStocks() {
        return stockManager.getStocksForDate(sysManager.getDate());
    }
    
    public List<StockProfile> getAvailableStockProfiles() {
        return stockManager.getStockProfilesForDate(sysManager.getDate());
    }
    
    public MovieData getMovieData(final String movieName) {
        return mManager.getMovieData(movieName);
    }
    
    public List<String> getTopMovieNames(final int rating, final int start, final int end) {
        return mManager.getTopMoviesInRange(rating, start, end);
    }
    
    public void resetDatastore() {
        String url = "jdbc:sqlite:/home/kjuu/classes/cmpsc174a/cs174a-stock-project/project/db/datastore.db";
        String[] tablesToClear = new String[] {"Sys_Info", "Accrue_Interest", "Customer", "Deposit", "Owns_Stock", "Market_Account", "Buy", "Movie", 
                "Movie_Contract", "Stock", "Withdraw", "Sell", "Stock_Profile" };

        String transaction = "BEGIN TRANSACTION;\n";

        for (int i = 0; i < tablesToClear.length; i++) {
            final String QUERY = "DELETE FROM " + tablesToClear[i] + ";\n";
            transaction += QUERY;
        }

        transaction += "END TRANSACTION;";

        try {
            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate(transaction);

            System.out.println("Successfully cleared all tables!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode()); 
        }

        try {
            // Read CSV file
            Scanner sc = new Scanner(new File("/home/kjuu/classes/cmpsc174a/cs174a-stock-project/project/db/data.csv"));
            sc.useDelimiter("\n");
            while (sc.hasNext()) {
                String entry = sc.next();
                String[] tokens = entry.split(",");
                String tableName = tokens[0];

                List<String> tokensWrapped = new ArrayList<String>();
                for (int i = 1; i < tokens.length; i++) {
                    boolean isNumeric = tokens[i].chars().allMatch( Character::isDigit );
                    if (isNumeric) {
                        tokensWrapped.add(tokens[i]);
                    } else {
                        tokensWrapped.add("\"" + tokens[i] + "\"");
                    }
                }
                
                final String QUERY = "INSERT INTO " + tableName + " VALUES(" + String.join(",", tokensWrapped.toArray(new String[tokensWrapped.size()])) + ");";
                
                Statement stmt = this.conn.createStatement();
                stmt.executeUpdate(QUERY);
                
                System.out.println("[SUCCESSFUL]: " + QUERY);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
                    System.out.println(e.getErrorCode()); 
        } catch (FileNotFoundException f) {
            System.out.println(f.getMessage());
        } 
    }

    public static boolean isAlpha(String str) {
        return str.matches("[a-zA-Z]+");
    }
}
