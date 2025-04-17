package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class LoginControllerTests {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private User mockUser;

    @Mock
    private Postgres mockPostgres;

    @Value("${app.secret}")
    private String secret;

    // Helper method to create a LoginRequest object
    private LoginRequest createLoginRequest(String username, String password) {
        LoginRequest request = new LoginRequest();
        request.username = username;
        request.password = password;
        return request;
    }

    // Helper method to mock a User object
    private User mockUser(String username, String hashedPassword, String token) {
        User user = mock(User.class);
        when(user.hashedPassword).thenReturn(hashedPassword);
        when(user.token(secret)).thenReturn(token);
        return user;
    }

    @Test
    public void login_ValidCredentials_ShouldReturnToken() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        String hashedPassword = "hashedPassword";
        String token = "validToken";

        LoginRequest request = createLoginRequest(username, password);
        User user = mockUser(username, hashedPassword, token);

        when(User.fetch(username)).thenReturn(user);
        when(Postgres.md5(password)).thenReturn(hashedPassword);

        // Act
        LoginResponse response = loginController.login(request);

        // Assert
        assertNotNull("Response should not be null", response);
        assertEquals("Token should match", token, response.token);
    }

    @Test
    public void login_InvalidPassword_ShouldThrowUnauthorized() {
        // Arrange
        String username = "testUser";
        String password = "wrongPassword";
        String hashedPassword = "hashedPassword";

        LoginRequest request = createLoginRequest(username, password);
        User user = mockUser(username, hashedPassword, null);

        when(User.fetch(username)).thenReturn(user);
        when(Postgres.md5(password)).thenReturn("wrongHashedPassword");

        // Act & Assert
        try {
            loginController.login(request);
            fail("Expected Unauthorized exception to be thrown");
        } catch (Unauthorized ex) {
            assertEquals("Exception message should match", "Access Denied", ex.getMessage());
        }
    }

    @Test
    public void login_NonExistentUser_ShouldThrowUnauthorized() {
        // Arrange
        String username = "nonExistentUser";
        String password = "testPassword";

        LoginRequest request = createLoginRequest(username, password);

        when(User.fetch(username)).thenReturn(null);

        // Act & Assert
        try {
            loginController.login(request);
            fail("Expected Unauthorized exception to be thrown");
        } catch (Unauthorized ex) {
            assertEquals("Exception message should match", "Access Denied", ex.getMessage());
        }
    }

    @Test
    public void login_NullRequest_ShouldThrowException() {
        // Act & Assert
        try {
            loginController.login(null);
            fail("Expected exception to be thrown");
        } catch (Exception ex) {
            assertTrue("Exception should be an instance of NullPointerException or similar", ex instanceof RuntimeException);
        }
    }
}
