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
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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

    public boolean logout() {
        if (isLoggedIn) {
            isLoggedIn = false;
	    user = null;
            return true;
        }
        
        return false;
    }

    public boolean createTrader(Customer c) {
        boolean res = this.userManager.createTrader(c);
        if (res) {
            isLoggedIn = true;
	    user = c;
        }
        return res;
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
