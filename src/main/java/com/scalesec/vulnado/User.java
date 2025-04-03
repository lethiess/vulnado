package com.scalesec.vulnado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class User {
  private String id; // User ID
  private String username; // Username
  private String hashedPassword; // Hashed password

  public User(String id, String username, String hashedPassword) {
    this.id = id;
    this.username = username;
    this.hashedPassword = hashedPassword;
  }

  public String token(String secret) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.builder().setSubject(this.username).signWith(key).compact();
  }

  public static void assertAuth(String secret, String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);
    } catch (Exception e) {
      throw new Unauthorized(e.getMessage());
    }
  }

  public static User fetch(String un) {
    Logger logger = Logger.getLogger(User.class.getName());
    User user = null;
    String query = "SELECT * FROM users WHERE username = ? LIMIT 1";

    try (Connection cxn = Postgres.connection();
         PreparedStatement pstmt = cxn.prepareStatement(query)) {
      pstmt.setString(1, un);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          String userId = rs.getString("user_id");
          String username = rs.getString("username");
          String password = rs.getString("password");
          user = new User(userId, username, password);
        }
      }
    } catch (Exception e) {
      logger.severe("Error fetching user: " + e.getMessage());
    }
    return user;
  }
}
