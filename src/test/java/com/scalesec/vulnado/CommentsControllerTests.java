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
    public void comments_ShouldReturnAllComments_WhenAuthTokenIsValid() {
        // Arrange
        String validToken = "valid-token";
        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(new Comment());
        mockComments.add(new Comment());

        doNothing().when(userMock).assertAuth(secret, validToken);
        when(Comment.fetch_all()).thenReturn(mockComments);

        // Act
        List<Comment> result = commentsController.comments(validToken);

        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should match mock comments size", mockComments.size(), result.size());
        verify(userMock, times(1)).assertAuth(secret, validToken);
        verify(commentMock, times(1)).fetch_all();
    }

    @Test(expected = ResponseStatusException.class)
    public void comments_ShouldThrowException_WhenAuthTokenIsInvalid() {
        // Arrange
        String invalidToken = "invalid-token";

        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).when(userMock).assertAuth(secret, invalidToken);

        // Act
        commentsController.comments(invalidToken);

        // Assert
        // Exception is expected
    }

    @Test
    public void createComment_ShouldCreateComment_WhenInputIsValid() {
        // Arrange
        String validToken = "valid-token";
        CommentRequest input = new CommentRequest();
        input.username = "testUser";
        input.body = "This is a test comment";

        Comment mockComment = new Comment();
        when(Comment.create(input.username, input.body)).thenReturn(mockComment);

        // Act
        Comment result = commentsController.createComment(validToken, input);

        // Assert
        assertNotNull("Result should not be null", result);
        verify(commentMock, times(1)).create(input.username, input.body);
    }

    @Test
    public void deleteComment_ShouldReturnTrue_WhenCommentIsDeleted() {
        // Arrange
        String validToken = "valid-token";
        String commentId = "123";

        when(Comment.delete(commentId)).thenReturn(true);

        // Act
        Boolean result = commentsController.deleteComment(validToken, commentId);

        // Assert
        assertTrue("Result should be true", result);
        verify(commentMock, times(1)).delete(commentId);
    }

    @Test
    public void deleteComment_ShouldReturnFalse_WhenCommentIsNotDeleted() {
        // Arrange
        String validToken = "valid-token";
        String commentId = "123";

        when(Comment.delete(commentId)).thenReturn(false);

        // Act
        Boolean result = commentsController.deleteComment(validToken, commentId);

        // Assert
        assertFalse("Result should be false", result);
        verify(commentMock, times(1)).delete(commentId);
    }
}
