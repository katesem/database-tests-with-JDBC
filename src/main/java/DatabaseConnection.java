import java.sql.*;
import java.util.Arrays;

public class DatabaseConnection {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://host.docker.internal:3306/jdbc_test_database";
        String user = "root";
        String password = "qweqwe12";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
            System.out.println("Connected to the database!");
            // Create a Statement
            Statement statement = connection.createStatement();

            // Create a table
            String createTableSQL = "CREATE TABLE users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "username VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL)";
            statement.executeUpdate(createTableSQL);
            System.out.println("Table created successfully!");

            //Data inserting
            String insertDataSQL = "INSERT INTO users (id, username, email) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertDataSQL)) {
                Object[][] values = {
                        {1, "user1", "user1@example.com"},
                        {2, "user2", "user2@example.com"},
                        {3, "user3", "user3@example.com"}
                };

                for (Object[] row : values) {
                    preparedStatement.setInt(1, (int) row[0]);
                    preparedStatement.setString(2, (String) row[1]);
                    preparedStatement.setString(3, (String) row[2]);
                    preparedStatement.addBatch();
                }

                int[] updateCounts = preparedStatement.executeBatch();
                System.out.println("Rows inserted: " + Arrays.toString(updateCounts));

                String selectDataSQL = "SELECT * FROM users";
                ResultSet resultSet = statement.executeQuery(selectDataSQL);
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    System.out.println("ID: " + id + ", Username: " + username + ", Email: " + email);
                }
                statement.close();
                connection.close();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

