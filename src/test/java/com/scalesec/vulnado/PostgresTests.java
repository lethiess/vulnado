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

    // Helper method to mock a database connection
    private Connection mockConnection() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(connection.createStatement()).thenReturn(statement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);

        return connection;
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
        Connection mockConnection = mockConnection();
        Postgres.setup();

        verify(mockConnection.createStatement(), atLeastOnce()).executeUpdate(anyString());
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
        Connection mockConnection = mockConnection();
        String username = "testuser";
        String password = "testpassword";

        Postgres.insertUser(username, password);

        verify(mockConnection.prepareStatement(anyString()), atLeastOnce()).executeUpdate();
    }

    @Test
    public void insertComment_ShouldInsertCommentIntoDatabase() throws Exception {
        Connection mockConnection = mockConnection();
        String username = "testuser";
        String body = "This is a test comment";

        Postgres.insertComment(username, body);

        verify(mockConnection.prepareStatement(anyString()), atLeastOnce()).executeUpdate();
    }
}
