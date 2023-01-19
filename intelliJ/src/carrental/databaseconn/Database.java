package carrental.databaseconn;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./src/db/";
    private Connection conn;

    public Database(String databaseName) {
        try {
            // Registering JDBC driver
            Class.forName(JDBC_DRIVER);
            // Open a connection
            conn = DriverManager.getConnection(DB_URL + databaseName);
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return conn;
    }

    // close connection
    public void exit() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
