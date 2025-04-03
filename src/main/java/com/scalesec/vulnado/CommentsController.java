package com.scalesec.vulnado;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;
import java.util.List;
import java.io.Serializable;

@RestController
@EnableAutoConfiguration
public class CommentsController {
  @Value("${app.secret}")
  private String secret;

  @CrossOrigin(origins = "*")
  @GetMapping(value = "/comments", produces = "application/json")
  List<Comment> comments(@RequestHeader(value="x-auth-token") String token) {
    User.assertAuth(secret, token);
    return Comment.fetch_all();
  }

  @CrossOrigin(origins = "*")
  @PostMapping(value = "/comments", produces = "application/json", consumes = "application/json")
  Comment createComment(@RequestHeader(value="x-auth-token") String token, @RequestBody CommentRequest input) {
    User.assertAuth(secret, token); // Ensure authentication
    if (input == null || input.username == null || input.username.isEmpty() || input.body == null || input.body.isEmpty()) {
      throw new BadRequest("Invalid input: username and body are required.");
    }
    return Comment.create(input.username, input.body);
  }

  @CrossOrigin(origins = "*")
  @DeleteMapping(value = "/comments/{id}", produces = "application/json")
  Boolean deleteComment(@RequestHeader(value="x-auth-token") String token, @PathVariable("id") String id) {
    User.assertAuth(secret, token); // Ensure authentication
    if (id == null || id.isEmpty()) {
      throw new BadRequest("Invalid comment ID.");
    }
    boolean result = Comment.delete(id);
    if (!result) {
      throw new BadRequest("Comment not found or could not be deleted.");
    }
    return result;
  }
}

class CommentRequest implements Serializable {
  private String username;
  private String body;

  // Add getters and setters for proper deserialization
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequest extends RuntimeException {
  public BadRequest(String exception) {
    super(exception);
  }
}

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class ServerError extends RuntimeException {
  public ServerError(String exception) {
    super(exception);
  }
}
