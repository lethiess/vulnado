Let's address the issues and hotspots reported by SonarQube one by one. We'll start with the first remark at line 12.

---

### **[12]:**
- **[ISSUE](java:S1118):** Add a private constructor to hide the implicit public one.

#### **Analysis:**
The `Postgres` class is a utility class with only static methods. To prevent instantiation, we need to add a private constructor.

#### **Fix:**
Add a private constructor to the `Postgres` class.

---

### **ContentEditor Changes for Line 12:**
```java
private Postgres() {
    throw new UnsupportedOperationException("Utility class");
}
```

---

### **[16]:**
- **[ISSUE](java:S4925):** Remove this `Class.forName()`, it is useless.

#### **Analysis:**
The `Class.forName()` call is unnecessary because the JDBC driver is automatically loaded when the `DriverManager` is used.

#### **Fix:**
Remove the line containing `Class.forName("org.postgresql.Driver");`.

---

### **ContentEditor Changes for Line 16:**
```java
// Remove the line entirely
```

---

### **[25]:**
- **[HOTSPOT](java:S4507):** Make sure this debug feature is deactivated before delivering the code in production.

#### **Analysis:**
The `e.printStackTrace()` is a debug feature and should not be used in production. Replace it with proper logging.

#### **Fix:**
Replace `e.printStackTrace()` with a logger.

---

### **ContentEditor Changes for Line 25:**
```java
LOGGER.error("An error occurred", e);
```

---

### **[26]:**
- **[ISSUE](java:S106):** Replace this use of `System.err` by a logger.

#### **Analysis:**
Replace `System.err.println` with a logger.

#### **Fix:**
Replace `System.err.println` with `LOGGER.error`.

---

### **ContentEditor Changes for Line 26:**
```java
LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
```

---

### **[33]:**
- **[ISSUE](java:S106):** Replace this use of `System.out` by a logger.

#### **Analysis:**
Replace `System.out.println` with a logger.

#### **Fix:**
Replace `System.out.println` with `LOGGER.info`.

---

### **ContentEditor Changes for Line 33:**
```java
LOGGER.info("Setting up Database...");
```

---

### **[35]:**
- **[ISSUE](java:S2095):** Use try-with-resources or close this `Statement` in a `finally` clause.

#### **Analysis:**
The `Statement` object is not closed properly. Use try-with-resources to ensure it is closed.

#### **Fix:**
Refactor the code to use try-with-resources for the `Statement`.

---

### **ContentEditor Changes for Line 35:**
```java
try (Statement stmt = c.createStatement()) {
    // Existing code for schema creation and data insertion
}
```

---

### **[56]:**
- **[ISSUE](java:S106):** Replace this use of `System.out` by a logger.

#### **Analysis:**
Replace `System.out.println` with a logger.

#### **Fix:**
Replace `System.out.println` with `LOGGER.error`.

---

### **ContentEditor Changes for Line 56:**
```java
LOGGER.error(e.getMessage(), e);
```

---

### **[67]:**
- **[HOTSPOT](java:S4790):** Make sure this weak hash algorithm is not used in a sensitive context here.

#### **Analysis:**
MD5 is a weak hash algorithm and should not be used in sensitive contexts. Replace it with a stronger algorithm like SHA-256.

#### **Fix:**
Replace `MessageDigest.getInstance("MD5")` with `MessageDigest.getInstance("SHA-256")`.

---

### **ContentEditor Changes for Line 67:**
```java
MessageDigest md = MessageDigest.getInstance("SHA-256");
```

---

### **[79]:**
- **[ISSUE](java:S1643):** Use a `StringBuilder` instead.

#### **Analysis:**
The code uses string concatenation in a loop. Replace it with a `StringBuilder`.

#### **Fix:**
Refactor the code to use a `StringBuilder`.

---

### **ContentEditor Changes for Line 79:**
```java
StringBuilder hashtextBuilder = new StringBuilder(hashtext);
while (hashtextBuilder.length() < 32) {
    hashtextBuilder.insert(0, "0");
}
hashtext = hashtextBuilder.toString();
```

---

### **[86]:**
- **[ISSUE](java:S112):** Define and throw a dedicated exception instead of using a generic one.

#### **Analysis:**
Replace `RuntimeException` with a custom exception.

#### **Fix:**
Define a custom exception class and use it.

---

### **ContentEditor Changes for Line 86:**
```java
throw new HashingException("Error while hashing", e);
```

---

### **[94]:**
- **[ISSUE](java:S2095):** Use try-with-resources or close this `PreparedStatement` in a `finally` clause.

#### **Analysis:**
The `PreparedStatement` is not closed properly. Use try-with-resources to ensure it is closed.

#### **Fix:**
Refactor the code to use try-with-resources for the `PreparedStatement`.

---

### **ContentEditor Changes for Line 94:**
```java
try (PreparedStatement pStatement = connection().prepareStatement(sql)) {
    // Existing code for setting parameters and executing the statement
}
```

---

### **[100]:**
- **[HOTSPOT](java:S4507):** Make sure this debug feature is deactivated before delivering the code in production.

#### **Analysis:**
Replace `e.printStackTrace()` with proper logging.

#### **Fix:**
Replace `e.printStackTrace()` with a logger.

---

### **ContentEditor Changes for Line 100:**
```java
LOGGER.error("An error occurred", e);
```

---

### **[108]:**
- **[ISSUE](java:S2095):** Use try-with-resources or close this `PreparedStatement` in a `finally` clause.

#### **Analysis:**
The `PreparedStatement` is not closed properly. Use try-with-resources to ensure it is closed.

#### **Fix:**
Refactor the code to use try-with-resources for the `PreparedStatement`.

---

### **ContentEditor Changes for Line 108:**
```java
try (PreparedStatement pStatement = connection().prepareStatement(sql)) {
    // Existing code for setting parameters and executing the statement
}
```

---

### **[114]:**
- **[HOTSPOT](java:S4507):** Make sure this debug feature is deactivated before delivering the code in production.

#### **Analysis:**
Replace `e.printStackTrace()` with proper logging.

#### **Fix:**
Replace `e.printStackTrace()` with a logger.

---

### **ContentEditor Changes for Line 114:**
```java
LOGGER.error("An error occurred", e);
```

---

Let me know if you'd like me to apply these changes!
