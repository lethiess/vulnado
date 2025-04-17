package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostgresTests {

    @Test
    public void connection_ShouldReturnValidConnection() {
        // Mock environment variables
        System.setProperty("PGHOST", "localhost");
        System.setProperty("PGDATABASE", "testdb");
        System.setProperty("PGUSER", "testuser");
        System.setProperty("PGPASSWORD", "testpassword");

        Connection connection = null;
        try {
            connection = Postgres.connection();
            assertNotNull("Connection should not be null", connection);
            assertFalse("Connection should not be closed", connection.isClosed());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    @Test
    public void setup_ShouldCreateTablesAndInsertData() {
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);

        try {
            when(mockConnection.createStatement()).thenReturn(mockStatement);

            // Mock the connection method
            Postgres mockPostgres = Mockito.spy(Postgres.class);
            doReturn(mockConnection).when(mockPostgres).connection();

            mockPostgres.setup();

            verify(mockStatement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS users(user_id VARCHAR (36) PRIMARY KEY, username VARCHAR (50) UNIQUE NOT NULL, password VARCHAR (50) NOT NULL, created_on TIMESTAMP NOT NULL, last_login TIMESTAMP)");
            verify(mockStatement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS comments(id VARCHAR (36) PRIMARY KEY, username VARCHAR (36), body VARCHAR (500), created_on TIMESTAMP NOT NULL)");
            verify(mockStatement, times(1)).executeUpdate("DELETE FROM users");
            verify(mockStatement, times(1)).executeUpdate("DELETE FROM comments");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void md5_ShouldReturnCorrectHash() {
        String input = "test";
        String expectedHash = "098f6bcd4621d373cade4e832627b4f6";

        String actualHash = Postgres.md5(input);

        assertEquals("MD5 hash should match expected value", expectedHash, actualHash);
    }

    @Test
    public void insertUser_ShouldInsertUserIntoDatabase() {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        try {
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

            // Mock the connection method
            Postgres mockPostgres = Mockito.spy(Postgres.class);
            doReturn(mockConnection).when(mockPostgres).connection();

            String username = "testuser";
            String password = "testpassword";

            mockPostgres.insertUser(username, password);

            verify(mockPreparedStatement, times(1)).setString(eq(1), anyString());
            verify(mockPreparedStatement, times(1)).setString(eq(2), eq(username));
            verify(mockPreparedStatement, times(1)).setString(eq(3), eq(Postgres.md5(password)));
            verify(mockPreparedStatement, times(1)).executeUpdate();
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void insertComment_ShouldInsertCommentIntoDatabase() {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        try {
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

            // Mock the connection method
            Postgres mockPostgres = Mockito.spy(Postgres.class);
            doReturn(mockConnection).when(mockPostgres).connection();

            String username = "testuser";
            String body = "This is a test comment";

            mockPostgres.insertComment(username, body);

            verify(mockPreparedStatement, times(1)).setString(eq(1), anyString());
            verify(mockPreparedStatement, times(1)).setString(eq(2), eq(username));
            verify(mockPreparedStatement, times(1)).setString(eq(3), eq(body));
            verify(mockPreparedStatement, times(1)).executeUpdate();
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
}
