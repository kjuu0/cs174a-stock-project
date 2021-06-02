package backend;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MarketAccountManager {
    private Connection conn;
    private SysManager sysManager;

    public MarketAccountManager(Connection c, SysManager sm) {
        conn = c; 
        sysManager = sm;
    }

    public List<MarketAccount> getAllMarketAccounts() {
        List<MarketAccount> accounts = new ArrayList<>();
        final String QUERY = "SELECT * from Market_Account";
        try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(QUERY);
           
            while(rs.next()) {
                accounts.add(new MarketAccount(rs.getInt("tax_id"), rs.getInt("balance")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }

        return accounts;
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
    
    public List<DepositTransaction> getAllDeposits(int taxid) {
        List<DepositTransaction> deposits = new ArrayList<>();
        final String QUERY = "SELECT * FROM Deposit WHERE tax_id=" + taxid + " ORDER BY timestamp DESC";

        try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(QUERY);
           
            while (rs.next()) {
                deposits.add(new DepositTransaction(
                    rs.getInt("transaction_id"),
                    rs.getString("transaction_date"),
                    rs.getLong("timestamp"),
                    rs.getInt("tax_id"),
                    rs.getInt("amount")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return deposits;
    }

    public List<DepositTransaction> getAllDepositsThisMonth(int taxid) {
        String year = sysManager.getYear();
        String month = sysManager.getMonth();
        List<DepositTransaction> deposits = new ArrayList<>();
        final String QUERY = "SELECT * FROM Deposit WHERE tax_id=" + taxid
            + " AND transaction_date LIKE \"" + year + "/" + month + "/" + "%\""
            + " ORDER BY timestamp DESC";

        try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(QUERY);
           
            while (rs.next()) {
                deposits.add(new DepositTransaction(
                    rs.getInt("transaction_id"),
                    rs.getString("transaction_date"),
                    rs.getLong("timestamp"),
                    rs.getInt("tax_id"),
                    rs.getInt("amount")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return deposits;
    }
    
    public List<WithdrawTransaction> getAllWithdraws(int taxid) {
        List<WithdrawTransaction> withdraws = new ArrayList<>();
        final String QUERY = "SELECT * FROM Withdraw WHERE tax_id=" + taxid + " ORDER BY timestamp DESC";

        try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(QUERY);
           
            while (rs.next()) {
                withdraws.add(new WithdrawTransaction(
                    rs.getInt("transaction_id"),
                    rs.getString("transaction_date"),
                    rs.getLong("timestamp"),
                    rs.getInt("tax_id"),
                    rs.getInt("amount")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return withdraws;
    }
    
    public List<WithdrawTransaction> getAllWithdrawsThisMonth(int taxid) {
        String year = sysManager.getYear();
        String month = sysManager.getMonth();
        List<WithdrawTransaction> withdraws = new ArrayList<>();
        final String QUERY = "SELECT * FROM Withdraw WHERE tax_id=" + taxid
            + " AND transaction_date LIKE \"" + year + "/" + month + "/" + "%\""
            + " ORDER BY timestamp DESC";

        try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(QUERY);
           
            while (rs.next()) {
                withdraws.add(new WithdrawTransaction(
                    rs.getInt("transaction_id"),
                    rs.getString("transaction_date"),
                    rs.getLong("timestamp"),
                    rs.getInt("tax_id"),
                    rs.getInt("amount")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return withdraws;
    }

    public void addInterest(int taxid, float rate) {
        int interest = Math.round(getBalance(taxid) * rate);
        final String INSERT_ACCRUE_INTEREST = "INSERT INTO Accrue_Interest"
            + "(transaction_date, timestamp, tax_id, amount, interest_rate)"
            + "VALUES (\"" + sysManager.getDate() + "\"," + System.currentTimeMillis() / 1000 + "," + taxid + "," + interest + "," + rate + ")";
        final String UPDATE_BALANCE = "UPDATE Market_Account SET balance = balance + " + interest + " WHERE tax_id = " + taxid;

        try {
            Statement stmt = conn.createStatement();
            stmt.addBatch(INSERT_ACCRUE_INTEREST);
            stmt.addBatch(UPDATE_BALANCE);
            stmt.executeBatch();

        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
    }
    
    public List<AccrueInterestTransaction> getAllAccruedInterests(int taxid) {
        List<AccrueInterestTransaction> accruedInterests = new ArrayList<>();
        final String QUERY = "SELECT * FROM Accrue_Interest WHERE tax_id=" + taxid + " ORDER BY timestamp DESC";

        try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(QUERY);
           
            while (rs.next()) {
                accruedInterests.add(new AccrueInterestTransaction(
                    rs.getInt("transaction_id"),
                    rs.getString("transaction_date"),
                    rs.getLong("timestamp"),
                    rs.getInt("tax_id"),
                    rs.getInt("amount"),
                    rs.getFloat("interest_rate")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return accruedInterests;
    }
    
    public List<AccrueInterestTransaction> getAllAccruedInterestsThisMonth(int taxid) {
        String year = sysManager.getYear();
        String month = sysManager.getMonth();
        List<AccrueInterestTransaction> accruedInterests = new ArrayList<>();
        final String QUERY = "SELECT * FROM Accrue_Interest WHERE tax_id=" + taxid
        + " AND transaction_date LIKE \"" + year + "/" + month + "/" + "%\""
        + " ORDER BY timestamp DESC";

        try {
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(QUERY);
           
            while (rs.next()) {
                accruedInterests.add(new AccrueInterestTransaction(
                    rs.getInt("transaction_id"),
                    rs.getString("transaction_date"),
                    rs.getLong("timestamp"),
                    rs.getInt("tax_id"),
                    rs.getInt("amount"),
                    rs.getFloat("interest_rate")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
        
        return accruedInterests;
    }
}
