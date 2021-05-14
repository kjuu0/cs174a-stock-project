package backend;

import java.sql.SQLException;
import java.sql.ResultSet;

public class Customer {
    public String name, username, password;
    public String address, state;
    public String phone, email, ssn;
    public int taxid, market_aid, balance;

    public Customer() {
    }
    
    public Customer(String name, String username, String password, String address, String state, String phone, String email, int taxid, String ssn, int market_aid, int balance) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.address = address;
        this.state = state;
        this.phone = phone;
        this.email = email;
        this.taxid = taxid;
        this.market_aid = market_aid;
        this.balance = balance;
    }

    public Customer(String name, String username, String password, String address, String state, String phone, String email, int taxid, String ssn) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.address = address;
        this.state = state;
        this.phone = phone;
        this.email = email;
        this.taxid = taxid;
    }

    public Customer(ResultSet rs) {
        try {
            this.name = rs.getString("name");
            this.username = rs.getString("username");
            this.password = rs.getString("password");
            this.address = rs.getString("address");
            this.state = rs.getString("state");
            this.phone = rs.getString("phone");
            this.email = rs.getString("email");
            this.taxid = rs.getInt("taxid");
            this.market_aid = rs.getInt("market_aid");
            this.balance = rs.getInt("balance");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }
}