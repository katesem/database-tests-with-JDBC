package com.core.datasetup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {

    private static final String jdbcUrl = "jdbc:mysql://host.docker.internal:3306/jdbc_test_database";
    private static final String user = "root";
    private static final String password = "qweqwe12";

    public static Connection connection;

    public static void initialize() throws SQLException {
        connection = DriverManager.getConnection(jdbcUrl, user, password);
    }

    //this method constructs creating table query with different count of parameters and types
    public static void createTableStatementConstructor(String tableName, String[] columnNames, String[] columnTypes) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            StringBuilder createTableSQL = new StringBuilder("CREATE TABLE ");
            createTableSQL.append(tableName).append(" (");

            for (int i = 0; i < columnNames.length; i++) {
                createTableSQL.append(columnNames[i]).append(" ").append(columnTypes[i]);
                if (i < columnNames.length - 1) {
                    createTableSQL.append(", ");
                }
            }
            createTableSQL.append(")");

            statement.executeUpdate(createTableSQL.toString());
        }
    }
 //   public static void insertTestData() throws SQLException {
 //   }

    public static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
