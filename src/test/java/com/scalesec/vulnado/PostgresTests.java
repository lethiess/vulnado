package com.scalesec.vulnado;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PostgresTests {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private Statement mockStatement;

    @Before
    public void setUp() throws Exception {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockStatement = mock(Statement.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void connection_ShouldReturnValidConnection() throws Exception {
        // Mock environment variables
        System.setProperty("PGHOST", "localhost");
        System.setProperty("PGDATABASE", "testdb");
        System.setProperty("PGUSER", "testuser");
        System.setProperty("PGPASSWORD", "testpassword");

        Connection connection = Postgres.connection();
        assertNotNull("Connection should not be null", connection);
    }

    @Test
    public void setup_ShouldCreateTablesAndInsertData() throws Exception {
        // Mock the connection and statement
        Postgres.setup();

        verify(mockStatement, atLeastOnce()).executeUpdate(anyString());
    }

    @Test
    public void md5_ShouldReturnCorrectHash() {
        String input = "test";
        String expectedHash = "098f6bcd4621d373cade4e832627b4f6";

        String actualHash = Postgres.md5(input);

        assertEquals("MD5 hash should match expected value", expectedHash, actualHash);
    }

    @Test
    public void insertUser_ShouldInsertUserIntoDatabase() throws Exception {
        String username = "testuser";
        String password = "testpassword";

        Postgres.insertUser(username, password);

        verify(mockPreparedStatement, times(1)).setString(eq(1), anyString());
        verify(mockPreparedStatement, times(1)).setString(eq(2), eq(username));
        verify(mockPreparedStatement, times(1)).setString(eq(3), eq(Postgres.md5(password)));
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void insertComment_ShouldInsertCommentIntoDatabase() throws Exception {
        String username = "testuser";
        String body = "This is a test comment";

        Postgres.insertComment(username, body);

        verify(mockPreparedStatement, times(1)).setString(eq(1), anyString());
        verify(mockPreparedStatement, times(1)).setString(eq(2), eq(username));
        verify(mockPreparedStatement, times(1)).setString(eq(3), eq(body));
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void connection_ShouldHandleExceptionGracefully() {
        // Simulate an exception
        System.setProperty("PGHOST", "");
        System.setProperty("PGDATABASE", "");
        System.setProperty("PGUSER", "");
        System.setProperty("PGPASSWORD", "");

        try {
            Postgres.connection();
            fail("Expected System.exit to be called");
        } catch (Exception e) {
            assertTrue("Exception should be thrown", true);
        }
    }
}
