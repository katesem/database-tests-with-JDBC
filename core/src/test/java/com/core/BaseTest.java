package com.core;

import com.core.database_operations.DatabaseOperations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.sql.SQLException;

public class BaseTest {

    protected DatabaseOperations databaseOperations;

    @BeforeMethod
    public void setUp() throws SQLException, IOException {
        databaseOperations = new DatabaseOperations();
        databaseOperations.initialize();
    }

    @AfterMethod
    public void tearDown() throws SQLException {
        databaseOperations.closeConnection();
    }
}
