Let's address the issues and hotspots reported by SonarQube one at a time. Starting with the first remark:

---

### **[12]: [ISSUE](java:S1118): Add a private constructor to hide the implicit public one.**

The `Postgres` class is a utility class with only static methods. To prevent instantiation, we need to add a private constructor.

---

### **Fix for Line 12**

We'll add a private constructor to the `Postgres` class to hide the implicit public one.

---

### **ContentEditor Operation**

```json
{
  "operation": "INSERT",
  "lineNumber": 13,
  "content": "    private Postgres() {"
},
{
  "operation": "INSERT",
  "lineNumber": 14,
  "content": "        throw new UnsupportedOperationException(\"Utility class\");"
},
{
  "operation": "INSERT",
  "lineNumber": 15,
  "content": "    }"
}
```

---

Let me know if I should proceed with this fix or move to the next remark.
