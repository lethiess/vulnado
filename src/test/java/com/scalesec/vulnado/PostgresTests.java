package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostgresTests {

    @Test
    public void connection_ShouldEstablishConnection() {
        try {
            Connection connection = Postgres.connection();
            assertNotNull("Connection should not be null", connection);
            assertFalse("Connection should be open", connection.isClosed());
            connection.close();
        } catch (Exception e) {
            fail("Exception occurred while testing connection: " + e.getMessage());
        }
    }

    @Test
    public void setup_ShouldCreateTablesAndSeedData() {
        try {
            Postgres.setup();
            Connection connection = Postgres.connection();
            Statement statement = connection.createStatement();

            // Verify users table exists and contains seed data
            ResultSet usersResultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM users");
            assertTrue("Users table should exist", usersResultSet.next());
            assertEquals("Users table should contain seed data", 5, usersResultSet.getInt("count"));

            // Verify comments table exists and contains seed data
            ResultSet commentsResultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM comments");
            assertTrue("Comments table should exist", commentsResultSet.next());
            assertEquals("Comments table should contain seed data", 2, commentsResultSet.getInt("count"));

            connection.close();
        } catch (Exception e) {
            fail("Exception occurred while testing setup: " + e.getMessage());
        }
    }

    @Test
    public void md5_ShouldReturnCorrectHash() {
        String input = "test";
        String expectedHash = "098f6bcd4621d373cade4e832627b4f6"; // Precomputed MD5 hash for "test"
        String actualHash = Postgres.md5(input);
        assertEquals("MD5 hash should match expected value", expectedHash, actualHash);
    }

    @Test
    public void insertUser_ShouldInsertUserIntoDatabase() {
        try {
            String username = "testUser";
            String password = "testPassword";
            Postgres.insertUser(username, password);

            Connection connection = Postgres.connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT username, password FROM users WHERE username = '" + username + "'");

            assertTrue("User should be inserted into database", resultSet.next());
            assertEquals("Username should match", username, resultSet.getString("username"));
            assertEquals("Password hash should match", Postgres.md5(password), resultSet.getString("password"));

            connection.close();
        } catch (Exception e) {
            fail("Exception occurred while testing insertUser: " + e.getMessage());
        }
    }

    @Test
    public void insertComment_ShouldInsertCommentIntoDatabase() {
        try {
            String username = "testUser";
            String body = "This is a test comment";
            Postgres.insertComment(username, body);

            Connection connection = Postgres.connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT username, body FROM comments WHERE username = '" + username + "' AND body = '" + body + "'");

            assertTrue("Comment should be inserted into database", resultSet.next());
            assertEquals("Username should match", username, resultSet.getString("username"));
            assertEquals("Comment body should match", body, resultSet.getString("body"));

            connection.close();
        } catch (Exception e) {
            fail("Exception occurred while testing insertComment: " + e.getMessage());
        }
    }
}
