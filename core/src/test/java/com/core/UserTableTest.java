package com.core;

import org.testng.annotations.Test;

import java.sql.SQLException;

import static com.core.constants.Constants.USERS_TABLE_NAME;

public class UserTableTest extends BaseTest {

    @Test
    public void testUserTableRowCount() throws SQLException {
        int users_row_count = 3;

        databaseOperations
                .verifyTableExists(USERS_TABLE_NAME)
                .verifyTableRowCount(USERS_TABLE_NAME, users_row_count);
    }
}
