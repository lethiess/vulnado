package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void user_Token_ShouldGenerateValidToken() {
        // Arrange
        String secret = "mysecretkey12345678901234567890"; // 32-byte key
        User user = new User("1", "testuser", "hashedpassword");

        // Act
        String token = user.token(secret);

        // Assert
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        String subject = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        assertEquals("testuser", subject, "Token subject should match the username");
    }

    @Test
    public void user_AssertAuth_ShouldValidateToken() {
        // Arrange
        String secret = "mysecretkey12345678901234567890"; // 32-byte key
        User user = new User("1", "testuser", "hashedpassword");
        String token = user.token(secret);

        // Act & Assert
        try {
            User.assertAuth(secret, token);
        } catch (Unauthorized e) {
            fail("Token validation should not throw an exception");
        }
    }

    @Test(expected = Unauthorized.class)
    public void user_AssertAuth_ShouldThrowUnauthorizedForInvalidToken() {
        // Arrange
        String secret = "mysecretkey12345678901234567890"; // 32-byte key
        String invalidToken = "invalidtoken";

        // Act
        User.assertAuth(secret, invalidToken);
    }

    @Test
    public void user_Fetch_ShouldReturnUser() throws Exception {
        // Arrange
        String username = "testuser";
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery("select * from users where username = '" + username + "' limit 1")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("user_id")).thenReturn("1");
        when(mockResultSet.getString("username")).thenReturn(username);
        when(mockResultSet.getString("password")).thenReturn("hashedpassword");

        Postgres.setConnection(mockConnection); // Assuming Postgres.connection() can be mocked

        // Act
        User user = User.fetch(username);

        // Assert
        assertNotNull(user, "User should not be null");
        assertEquals("1", user.id, "User ID should match");
        assertEquals(username, user.username, "Username should match");
        assertEquals("hashedpassword", user.hashedPassword, "Password should match");
    }

    @Test
    public void user_Fetch_ShouldReturnNullForNonExistentUser() throws Exception {
        // Arrange
        String username = "nonexistentuser";
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery("select * from users where username = '" + username + "' limit 1")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Postgres.setConnection(mockConnection); // Assuming Postgres.connection() can be mocked

        // Act
        User user = User.fetch(username);

        // Assert
        assertNull(user, "User should be null for non-existent username");
    }
}
