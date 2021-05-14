package project.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Controller {
    public Controller() {
        dbConnect();
    }

    public static void dbConnect() {
        Connection conn = null;
        System.out.println("reached");
        try {
            // db parameters
            String url = "jdbc:sqlite:/home/htransteven/ucsb/cs174a/cs174a-stock-project/project/db/datastore.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}