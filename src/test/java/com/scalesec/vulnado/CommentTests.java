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

    // Test for Comment.create method
    @Test
    public void create_ValidComment_ShouldReturnComment() throws Exception {
        // Mock Postgres connection
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        Postgres.setMockConnection(mockConnection);

        Comment comment = Comment.create("testUser", "testBody");

        assertNotNull("Comment should not be null", comment);
        assertEquals("Username should match", "testUser", comment.username);
        assertEquals("Body should match", "testBody", comment.body);
    }

    @Test(expected = BadRequest.class)
    public void create_InvalidComment_ShouldThrowBadRequest() throws Exception {
        // Mock Postgres connection
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        Postgres.setMockConnection(mockConnection);

        Comment.create("testUser", "testBody");
    }

    @Test(expected = ServerError.class)
    public void create_Exception_ShouldThrowServerError() throws Exception {
        // Mock Postgres connection
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        Postgres.setMockConnection(mockConnection);

        Comment.create("testUser", "testBody");
    }

    // Test for Comment.fetch_all method
    @Test
    public void fetchAll_ValidComments_ShouldReturnList() throws Exception {
        // Mock Postgres connection
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("id")).thenReturn("1", "2");
        when(mockResultSet.getString("username")).thenReturn("user1", "user2");
        when(mockResultSet.getString("body")).thenReturn("body1", "body2");
        when(mockResultSet.getTimestamp("created_on")).thenReturn(new Timestamp(System.currentTimeMillis()));

        Postgres.setMockConnection(mockConnection);

        List<Comment> comments = Comment.fetch_all();

        assertNotNull("Comments list should not be null", comments);
        assertEquals("Comments list size should match", 2, comments.size());
        assertEquals("First comment username should match", "user1", comments.get(0).username);
        assertEquals("Second comment username should match", "user2", comments.get(1).username);
    }

    @Test
    public void fetchAll_NoComments_ShouldReturnEmptyList() throws Exception {
        // Mock Postgres connection
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Postgres.setMockConnection(mockConnection);

        List<Comment> comments = Comment.fetch_all();

        assertNotNull("Comments list should not be null", comments);
        assertTrue("Comments list should be empty", comments.isEmpty());
    }

    // Test for Comment.delete method
    @Test
    public void delete_ValidId_ShouldReturnTrue() throws Exception {
        // Mock Postgres connection
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        Postgres.setMockConnection(mockConnection);

        boolean result = Comment.delete("1");

        assertTrue("Delete should return true", result);
    }

    @Test
    public void delete_InvalidId_ShouldReturnFalse() throws Exception {
        // Mock Postgres connection
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        Postgres.setMockConnection(mockConnection);

        boolean result = Comment.delete("invalidId");

        assertFalse("Delete should return false", result);
    }

    @Test
    public void delete_Exception_ShouldReturnFalse() throws Exception {
        // Mock Postgres connection
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        Postgres.setMockConnection(mockConnection);

        boolean result = Comment.delete("1");

        assertFalse("Delete should return false", result);
    }
}
