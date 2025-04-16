package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTests {

    private static final String SECRET_KEY = "mysecretkey123456789012345678901234"; // Mock secret key for testing
    private static final String VALID_TOKEN = "validToken";
    private static final String INVALID_TOKEN = "invalidToken";

    // Helper method to create a mock ResultSet
    private ResultSet createMockResultSet(String userId, String username, String password) throws Exception {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("user_id")).thenReturn(userId);
        when(resultSet.getString("username")).thenReturn(username);
        when(resultSet.getString("password")).thenReturn(password);
        return resultSet;
    }

    @Test
    public void token_ShouldGenerateValidToken() {
        // Arrange
        User user = new User("1", "testUser", "hashedPassword");

        // Act
        String token = user.token(SECRET_KEY);

        // Assert
        assertNotNull("Token should not be null", token);
        assertFalse("Token should not be empty", token.isEmpty());
    }

    @Test
    public void assertAuth_WithValidToken_ShouldNotThrowException() {
        // Arrange
        User user = new User("1", "testUser", "hashedPassword");
        String token = user.token(SECRET_KEY);

        // Act & Assert
        try {
            User.assertAuth(SECRET_KEY, token);
        } catch (Exception e) {
            fail("Valid token should not throw an exception");
        }
    }

    @Test(expected = Unauthorized.class)
    public void assertAuth_WithInvalidToken_ShouldThrowUnauthorized() {
        // Act
        User.assertAuth(SECRET_KEY, INVALID_TOKEN);
    }

    @Test
    public void fetch_WithValidUsername_ShouldReturnUser() throws Exception {
        // Arrange
        String username = "testUser";
        String userId = "1";
        String password = "hashedPassword";

        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = createMockResultSet(userId, username, password);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        // Mock Postgres connection
        Postgres mockPostgres = mock(Postgres.class);
        when(Postgres.connection()).thenReturn(mockConnection);

        // Act
        User user = User.fetch(username);

        // Assert
        assertNotNull("User should not be null", user);
        assertEquals("User ID should match", userId, user.id);
        assertEquals("Username should match", username, user.username);
        assertEquals("Password should match", password, user.hashedPassword);
    }

    @Test
    public void fetch_WithInvalidUsername_ShouldReturnNull() throws Exception {
        // Arrange
        String username = "nonExistentUser";

        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.next()).thenReturn(false);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        // Mock Postgres connection
        Postgres mockPostgres = mock(Postgres.class);
        when(Postgres.connection()).thenReturn(mockConnection);

        // Act
        User user = User.fetch(username);

        // Assert
        assertNull("User should be null for non-existent username", user);
    }
}
