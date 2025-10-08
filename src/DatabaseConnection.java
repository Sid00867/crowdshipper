import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {
    private static final String HOST = "localhost";
    private static final String PORT = "5432";
    private static final String DATABASE = "crowdship";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "jeffrey@123";

    // PostgreSQL JDBC URL format
    private static final String URL = String.format("jdbc:postgresql://%s:%s/%s", HOST, PORT, DATABASE);

    /**
     * Establishes connection to PostgreSQL database
     * 
     * @return Connection object or null if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

            // Set connection properties
            Properties props = new Properties();
            props.setProperty("user", USERNAME);
            props.setProperty("password", PASSWORD);
            props.setProperty("ssl", "false"); // Set to true for SSL connections
            props.setProperty("ApplicationName", "CrowdShipping-App");

            // Establish connection
            Connection conn = DriverManager.getConnection(URL, props);
            System.out.println("Successfully connected to PostgreSQL database: " + DATABASE);
            return conn;

        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found. Make sure postgresql.jar is in classpath.", e);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Safely closes database connection
     * 
     * @param conn Connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    System.out.println("Database connection closed.");
                }
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    /**
     * Tests database connectivity
     * 
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                // Test with a simple query
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery("SELECT version()");
                    if (rs.next()) {
                        System.out.println("PostgreSQL Version: " + rs.getString(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
        }
        return false;
    }
}
