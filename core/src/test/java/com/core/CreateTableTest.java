package com.core;

import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CreateTableTest extends BaseTest {

    @Test
    public void createTableTest() throws SQLException {
        String tableName = "people age";
        String[] columnNames = {"id", "name", "age_value"};
        String[] columnTypes = {"INT PRIMARY KEY AUTO_INCREMENT", "VARCHAR(255)", "INT"};
        Object[] values = {1, "John", 30};
        int rowCount = 1;

        List<Object[]> expectedRows = new ArrayList<>();
        expectedRows.add(values); // first row of table

        databaseOperations
                .createTable(tableName, columnNames, columnTypes)
                .fillTableFields(tableName, columnNames, values)
                .verifyTableRowCount(tableName, rowCount)
                .verifyRowValues(tableName, expectedRows);
    }
}