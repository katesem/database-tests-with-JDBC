package com.core.database_operations;

import com.core.configuration.ConfigurationManager;
import org.testng.Assert;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseOperations {
    public DatabaseOperations() throws IOException {
    }

    private Properties properties = ConfigurationManager.loadProperties();
    private String jdbcUrl = properties.getProperty("jdbc.url");
    private String user = properties.getProperty("jdbc.user");
    private String password = properties.getProperty("jdbc.password");

    private Connection connection;


    public void initialize() throws SQLException {
        connection = DriverManager.getConnection(jdbcUrl, user, password);
    }

    public DatabaseOperations verifyTableRowCount(String tableName, int expRowCount) throws SQLException {
        ResultSet resultSet = executeSelectQuery(tableName);
        int rowCount = 0;
        while (resultSet.next()) {
            rowCount++;
        }
        Assert.assertEquals(rowCount, expRowCount, String.format("Expected %d rows in the %s table", expRowCount, tableName));
        return this;
    }

    public DatabaseOperations verifyTableExists(String tableName) throws SQLException {
        boolean tableExists = checkIfTableExists(tableName);
        Assert.assertTrue(tableExists, "Table " + tableName + " doesn't exist.");
        return this;
    }

    public DatabaseOperations createDatabase(String databaseName) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE DATABASE " + databaseName);
        Assert.assertTrue(checkIfDatabaseExists(databaseName));
        return this;
    }

    //this method constructs creating table query with different count of parameters and types
    public void createTableStatementConstructor(String tableName, String[] columnNames, String[] columnTypes) throws SQLException {
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

    private ResultSet executeSelectQuery(String tableName) throws SQLException {
        String selectTableQuery = "SELECT * FROM " + tableName;
        Statement statement = connection.createStatement();
        return statement.executeQuery(selectTableQuery);
    }

    private boolean checkIfTableExists(String tableName) throws SQLException {
        ResultSet resultSet = connection.getMetaData().getTables(null, null, tableName, null);
        return resultSet.next();
    }

    private boolean checkIfDatabaseExists(String databaseName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = null;
        try {
            resultSet = metaData.getCatalogs();
            while (resultSet.next()) {
                String existingDatabaseName = resultSet.getString("TABLE_CAT");
                if (databaseName.equalsIgnoreCase(existingDatabaseName)) {
                    return true;
                }
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return false;
    }

    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
