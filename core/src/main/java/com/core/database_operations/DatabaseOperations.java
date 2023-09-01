package com.core.database_operations;

import com.core.configuration.ConfigurationManager;
import org.testng.Assert;

import java.io.IOException;
import java.sql.*;
import java.util.List;
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
        ResultSet resultSet = executeSelectAllQuery(tableName);
        int rowCount = 0;
        while (resultSet.next()) {
            rowCount++;
        }
        Assert.assertEquals(rowCount, expRowCount, String.format("Expected %d rows in the %s table", expRowCount, tableName));
        return this;
    }

    public DatabaseOperations verifyRowValues(String tableName, List<Object[]> expectedRows) throws SQLException {
        ResultSet resultSet = executeSelectAllQuery(tableName);
        int rowNumber = 0;
        while (resultSet.next() && rowNumber < expectedRows.size()) {
            Object[] expectedValues = expectedRows.get(rowNumber);

            for (int i = 0; i < expectedValues.length; i++) {
                Object expectedValue = expectedValues[i];
                Object actualValue = resultSet.getObject(i + 1);

                Assert.assertEquals(actualValue, expectedValue, String.format("Value mismatch for column %d in row %d: Expected %s, but found %s", i + 1, rowNumber + 1, expectedValue, actualValue));
            }

            rowNumber++;
        }

        if (rowNumber != expectedRows.size()) {
            throw new AssertionError("Number of rows returned from the query does not match the number of expected rows.");
        }

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

    // this method constructs creating table query with different count of parameters and types
    public DatabaseOperations createTable(String tableName, String[] columnNames, String[] columnTypes) throws SQLException {
        if (!checkIfTableExists(tableName)) {
            StringBuilder createTableSQL = new StringBuilder("CREATE TABLE ");
            createTableSQL.append(tableName).append(" (");

            for (int i = 0; i < columnNames.length; i++) {
                createTableSQL.append("`").append(columnNames[i]).append("` ").append(columnTypes[i]);
                if (i < columnNames.length - 1) {
                    createTableSQL.append(", ");
                }
            }
            createTableSQL.append(")");

            try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL.toString())) {
                preparedStatement.executeUpdate();
            }

            boolean tableCreated = checkIfTableExists(tableName);
            if (!tableCreated) {
                throw new SQLException("Failed to create table: " + tableName);
            }
        }
        return this;
    }



    public DatabaseOperations fillTableFields(String tableName, String[] columnNames, Object[] values) throws SQLException {
        if (columnNames.length != values.length) {
            throw new IllegalArgumentException("Number of column names must match the number of values.");
        }

        StringBuilder insertSQL = new StringBuilder("INSERT INTO ");
        insertSQL.append(tableName).append(" (");

        for (int i = 0; i < columnNames.length; i++) {
            insertSQL.append(columnNames[i]);
            if (i < columnNames.length - 1) {
                insertSQL.append(", ");
            }
        }
        insertSQL.append(") VALUES (");

        for (int i = 0; i < values.length; i++) {
            insertSQL.append("?");
            if (i < values.length - 1) {
                insertSQL.append(", ");
            }
        }
        insertSQL.append(")");

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL.toString())) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }
            preparedStatement.executeUpdate();
        }

        return this;
    }


    private ResultSet executeSelectAllQuery(String tableName) throws SQLException {
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
