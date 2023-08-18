package com.core.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserTableTest extends BaseTest {

    @Test
    public void testUserTableRowCount() throws SQLException {
        String selectTableQuery = "SELECT * FROM users";
        ResultSet resultSet = connection.createStatement().executeQuery(selectTableQuery);

        int rowCount = 0;
        while (resultSet.next()) {
            rowCount++;
        }
        Assert.assertEquals(rowCount, 3, "Expected 3 rows in the 'users' table");
    }
}
