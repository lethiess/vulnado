package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentsControllerTests {

    @InjectMocks
    private CommentsController commentsController;

    @Mock
    private User userMock;

    @Mock
    private Comment commentMock;

    @Value("${app.secret}")
    private String secret;

    public CommentsControllerTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void comments_WithValidToken_ShouldReturnComments() {
        // Arrange
        String token = "valid-token";
        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(new Comment());
        mockComments.add(new Comment());

        mockStatic(User.class);
        mockStatic(Comment.class);

        when(() -> User.assertAuth(secret, token)).thenReturn(true);
        when(Comment.fetch_all()).thenReturn(mockComments);

        // Act
        List<Comment> result = commentsController.comments(token);

        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should match mock comments size", mockComments.size(), result.size());
    }

    @Test(expected = ResponseStatusException.class)
    public void comments_WithInvalidToken_ShouldThrowException() {
        // Arrange
        String token = "invalid-token";

        mockStatic(User.class);
        when(() -> User.assertAuth(secret, token)).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // Act
        commentsController.comments(token);
    }

    @Test
    public void createComment_WithValidInput_ShouldReturnCreatedComment() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = new CommentRequest();
        input.username = "testUser";
        input.body = "testBody";

        Comment mockComment = new Comment();

        mockStatic(User.class);
        mockStatic(Comment.class);

        when(() -> User.assertAuth(secret, token)).thenReturn(true);
        when(Comment.create(input.username, input.body)).thenReturn(mockComment);

        // Act
        Comment result = commentsController.createComment(token, input);

        // Assert
        assertNotNull("Result should not be null", result);
    }

    @Test(expected = ResponseStatusException.class)
    public void createComment_WithInvalidToken_ShouldThrowException() {
        // Arrange
        String token = "invalid-token";
        CommentRequest input = new CommentRequest();
        input.username = "testUser";
        input.body = "testBody";

        mockStatic(User.class);
        when(() -> User.assertAuth(secret, token)).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // Act
        commentsController.createComment(token, input);
    }

    @Test
    public void deleteComment_WithValidId_ShouldReturnTrue() {
        // Arrange
        String token = "valid-token";
        String commentId = "123";

        mockStatic(User.class);
        mockStatic(Comment.class);

        when(() -> User.assertAuth(secret, token)).thenReturn(true);
        when(Comment.delete(commentId)).thenReturn(true);

        // Act
        Boolean result = commentsController.deleteComment(token, commentId);

        // Assert
        assertTrue("Result should be true", result);
    }

    @Test(expected = ResponseStatusException.class)
    public void deleteComment_WithInvalidToken_ShouldThrowException() {
        // Arrange
        String token = "invalid-token";
        String commentId = "123";

        mockStatic(User.class);
        when(() -> User.assertAuth(secret, token)).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // Act
        commentsController.deleteComment(token, commentId);
    }

    @Test
    public void deleteComment_WithInvalidId_ShouldReturnFalse() {
        // Arrange
        String token = "valid-token";
        String commentId = "invalid-id";

        mockStatic(User.class);
        mockStatic(Comment.class);

        when(() -> User.assertAuth(secret, token)).thenReturn(true);
        when(Comment.delete(commentId)).thenReturn(false);

        // Act
        Boolean result = commentsController.deleteComment(token, commentId);

        // Assert
        assertFalse("Result should be false", result);
    }
}
