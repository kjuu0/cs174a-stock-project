package backend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SysManager {
    private Connection conn;

    public SysManager(Connection c) {
        conn = c;
    }

    public String getDate() {
        final String QUERY = "SELECT date FROM Sys_Info";        

        try {
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
            return rs.getString("date");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
    
    public boolean isMarketOpen() {
        final String QUERY = "SELECT market_status FROM Sys_Info";

        try {
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
            return rs.getInt("market_status") == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }
}
