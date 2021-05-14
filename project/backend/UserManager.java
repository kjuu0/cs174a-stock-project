package backend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private Connection conn;

    public UserManager(Connection c) {
        this.conn = c;
    }

    public boolean createTrader(Customer c) {
        String[] tokens = new String[] {c.name, c.username, c.password, c.address, c.state, c.phone, c.email, String.valueOf(c.taxid), c.ssn, "(SELECT MAX(market_aid) + 1 FROM Customer)", "0"};
        List<String> tokensWrapped = new ArrayList<String>();
        for (int i = 0; i < tokens.length; i++) {
            boolean isNumeric = tokens[i].chars().allMatch( Character::isDigit );
            if (i == tokens.length - 2 || isNumeric) {
                tokensWrapped.add(tokens[i]);
            } else {
                tokensWrapped.add("\"" + tokens[i] + "\"");
            }
        }
        final String QUERY = "INSERT INTO Customer VALUES(" + String.join(",", tokensWrapped.toArray(new String[tokensWrapped.size()])) + ");";
        
        System.out.println(QUERY);

        try {
            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate(QUERY);

            System.out.println("Created a new user with the username " + c.username + "!");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }
}