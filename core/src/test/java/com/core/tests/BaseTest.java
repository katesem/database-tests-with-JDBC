package com.core.tests;

import com.core.datasetup.DatabaseSetup;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.sql.Connection;
import java.sql.SQLException;

public class BaseTest {

    protected Connection connection;

    @BeforeMethod
    public void setUp() throws SQLException {
        DatabaseSetup.initialize();
        connection = DatabaseSetup.connection;
    }

    @AfterMethod
    public void tearDown() throws SQLException {
        DatabaseSetup.closeConnection();
    }
}
