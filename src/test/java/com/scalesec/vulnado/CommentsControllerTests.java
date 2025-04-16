package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

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
    private User user;

    @Mock
    private Comment comment;

    @Value("${app.secret}")
    private String secret;

    public CommentsControllerTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void comments_ShouldReturnAllComments_WhenAuthIsValid() {
        // Arrange
        String token = "valid-token";
        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(new Comment());
        mockComments.add(new Comment());

        doNothing().when(user).assertAuth(secret, token);
        when(Comment.fetch_all()).thenReturn(mockComments);

        // Act
        List<Comment> result = commentsController.comments(token);

        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should match mock comments size", mockComments.size(), result.size());
        verify(user, times(1)).assertAuth(secret, token);
        verify(Comment, times(1)).fetch_all();
    }

    @Test(expected = ResponseStatusException.class)
    public void comments_ShouldThrowException_WhenAuthIsInvalid() {
        // Arrange
        String token = "invalid-token";

        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).when(user).assertAuth(secret, token);

        // Act
        commentsController.comments(token);

        // Assert
        // Exception is expected
    }

    @Test
    public void createComment_ShouldCreateComment_WhenInputIsValid() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = new CommentRequest();
        input.username = "testUser";
        input.body = "testBody";

        Comment mockComment = new Comment();
        when(Comment.create(input.username, input.body)).thenReturn(mockComment);

        // Act
        Comment result = commentsController.createComment(token, input);

        // Assert
        assertNotNull("Result should not be null", result);
        verify(Comment, times(1)).create(input.username, input.body);
    }

    @Test
    public void deleteComment_ShouldReturnTrue_WhenCommentIsDeleted() {
        // Arrange
        String token = "valid-token";
        String commentId = "123";

        when(Comment.delete(commentId)).thenReturn(true);

        // Act
        Boolean result = commentsController.deleteComment(token, commentId);

        // Assert
        assertTrue("Result should be true", result);
        verify(Comment, times(1)).delete(commentId);
    }

    @Test
    public void deleteComment_ShouldReturnFalse_WhenCommentIsNotDeleted() {
        // Arrange
        String token = "valid-token";
        String commentId = "123";

        when(Comment.delete(commentId)).thenReturn(false);

        // Act
        Boolean result = commentsController.deleteComment(token, commentId);

        // Assert
        assertFalse("Result should be false", result);
        verify(Comment, times(1)).delete(commentId);
    }
}
