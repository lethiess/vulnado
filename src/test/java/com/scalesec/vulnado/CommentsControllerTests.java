package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentsControllerTests {

    @Autowired
    private CommentsController commentsController;

    @Value("${app.secret}")
    private String secret;

    // Mock dependencies
    private User mockUser = mock(User.class);
    private Comment mockComment = mock(Comment.class);

    // Helper method to create a mock Comment object
    private Comment createMockComment(String id, String username, String body) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setUsername(username);
        comment.setBody(body);
        return comment;
    }

    @Test
    public void comments_ShouldReturnListOfComments_WhenAuthenticated() {
        // Arrange
        String token = "valid-token";
        List<Comment> expectedComments = Arrays.asList(
            createMockComment("1", "user1", "This is a comment"),
            createMockComment("2", "user2", "Another comment")
        );
        when(mockUser.assertAuth(secret, token)).thenReturn(true);
        when(mockComment.fetch_all()).thenReturn(expectedComments);

        // Act
        List<Comment> actualComments = commentsController.comments(token);

        // Assert
        assertEquals("Expected comments list does not match actual", expectedComments, actualComments);
    }

    @Test(expected = ResponseStatusException.class)
    public void comments_ShouldThrowException_WhenNotAuthenticated() {
        // Arrange
        String token = "invalid-token";
        when(mockUser.assertAuth(secret, token)).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // Act
        commentsController.comments(token);
    }

    @Test
    public void createComment_ShouldReturnCreatedComment() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = new CommentRequest();
        input.username = "user1";
        input.body = "This is a new comment";
        Comment expectedComment = createMockComment("3", input.username, input.body);
        when(mockComment.create(input.username, input.body)).thenReturn(expectedComment);

        // Act
        Comment actualComment = commentsController.createComment(token, input);

        // Assert
        assertEquals("Expected created comment does not match actual", expectedComment, actualComment);
    }

    @Test
    public void deleteComment_ShouldReturnTrue_WhenCommentDeleted() {
        // Arrange
        String token = "valid-token";
        String commentId = "1";
        when(mockComment.delete(commentId)).thenReturn(true);

        // Act
        Boolean result = commentsController.deleteComment(token, commentId);

        // Assert
        assertTrue("Expected true when comment is deleted", result);
    }

    @Test
    public void deleteComment_ShouldReturnFalse_WhenCommentNotDeleted() {
        // Arrange
        String token = "valid-token";
        String commentId = "1";
        when(mockComment.delete(commentId)).thenReturn(false);

        // Act
        Boolean result = commentsController.deleteComment(token, commentId);

        // Assert
        assertFalse("Expected false when comment is not deleted", result);
    }
}

//BEGIN: Work/DemoTestCreator/2025-04-03__15-15-14.264__GenerateTests/Input/Existing_Tests/VulnadoApplicationTests.java
package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

	@Test
	public void contextLoads() {
	}

}


//END: Work/DemoTestCreator/2025-04-03__15-15-14.264__GenerateTests/Input/Existing_Tests/VulnadoApplicationTests.java
