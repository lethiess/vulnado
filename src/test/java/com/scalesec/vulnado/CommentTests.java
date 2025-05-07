package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentTests {

    // Helper method to create a mock connection
    private Connection createMockConnection() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        return mockConnection;
    }

    // Helper method to create a mock result set
    private ResultSet createMockResultSet() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("id")).thenReturn("123");
        when(mockResultSet.getString("username")).thenReturn("test_user");
        when(mockResultSet.getString("body")).thenReturn("test_body");
        when(mockResultSet.getTimestamp("created_on")).thenReturn(new Timestamp(System.currentTimeMillis()));
        return mockResultSet;
    }

    @Test
    public void create_ShouldReturnComment_WhenValidInput() throws SQLException {
        // Arrange
        String username = "test_user";
        String body = "test_body";
        Connection mockConnection = createMockConnection();
        Postgres.setMockConnection(mockConnection);

        // Act
        Comment comment = Comment.create(username, body);

        // Assert
        assertNotNull("Comment should not be null", comment);
        assertEquals("Username should match", username, comment.username);
        assertEquals("Body should match", body, comment.body);
    }

    @Test(expected = ServerError.class)
    public void create_ShouldThrowServerError_WhenCommitFails() throws SQLException {
        // Arrange
        String username = "test_user";
        String body = "test_body";
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));
        Postgres.setMockConnection(mockConnection);

        // Act
        Comment.create(username, body);
    }

    @Test
    public void fetchAll_ShouldReturnListOfComments() throws SQLException {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = createMockResultSet();
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        Postgres.setMockConnection(mockConnection);

        // Act
        List<Comment> comments = Comment.fetch_all();

        // Assert
        assertNotNull("Comments list should not be null", comments);
        assertEquals("Comments list size should be 1", 1, comments.size());
        assertEquals("Comment username should match", "test_user", comments.get(0).username);
    }

    @Test
    public void delete_ShouldReturnTrue_WhenCommentExists() throws SQLException {
        // Arrange
        String commentId = "123";
        Connection mockConnection = createMockConnection();
        Postgres.setMockConnection(mockConnection);

        // Act
        boolean result = Comment.delete(commentId);

        // Assert
        assertTrue("Delete should return true for existing comment", result);
    }

    @Test
    public void delete_ShouldReturnFalse_WhenCommentDoesNotExist() throws SQLException {
        // Arrange
        String commentId = "123";
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        Postgres.setMockConnection(mockConnection);

        // Act
        boolean result = Comment.delete(commentId);

        // Assert
        assertFalse("Delete should return false for non-existing comment", result);
    }
}
