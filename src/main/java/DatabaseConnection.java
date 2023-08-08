import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://host.docker.internal:3306/jdbc_test_database";
        String user = "root";
        String password = "qweqwe12";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
            System.out.println("Connected to the database!");
            // Perform database operations here
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
