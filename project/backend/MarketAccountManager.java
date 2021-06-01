package backend;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MarketAccountManager {
    private Connection conn;
    private SysManager sysManager;

    public MarketAccountManager(Connection c, SysManager sm) {
        conn = c; 
        sysManager = sm;
    }
    
    public int getBalance(int taxid) {
        final String QUERY = "SELECT balance FROM Market_Account WHERE tax_id=" + taxid; 
        try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(QUERY);
           
            if (rs.next()) {
                return rs.getInt("balance");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return -1;
    }

    public void deposit(int taxid, int value) {
        final String INSERT_DEPOSIT = "INSERT INTO Deposit "
        + "(transaction_date, timestamp, tax_id, amount) "
        + "VALUES (\"" + sysManager.getDate() + "\"," + System.currentTimeMillis() / 1000 + "," + taxid + "," + value + ")";
        final String UPDATE_BALANCE = "UPDATE Market_Account SET balance = balance + " + value + " WHERE tax_id = " + taxid;

        try {
            Statement stmt = conn.createStatement();
            stmt.addBatch(INSERT_DEPOSIT);
            stmt.addBatch(UPDATE_BALANCE);
            stmt.executeBatch();

        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
    }

    public boolean withdraw(int taxid, int value) {
        if (value > this.getBalance(taxid)) {
            return false;
        }

        final String INSERT_WITHDRAW = "INSERT INTO Withdraw "
        + "(transaction_date, timestamp, tax_id, amount) "
        + "VALUES (\"" + sysManager.getDate() + "\"," + System.currentTimeMillis() / 1000 + "," + taxid + "," + value + ")";
        final String UPDATE_BALANCE = "UPDATE Market_Account SET balance = balance - " + value + " WHERE tax_id = " + taxid;

        try {
            Statement stmt = conn.createStatement();
            stmt.addBatch(INSERT_WITHDRAW);
            stmt.addBatch(UPDATE_BALANCE);
            stmt.executeBatch();

        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
            return false;
        }

        return true;
    }
}
