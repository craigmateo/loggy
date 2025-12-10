# Loggy

Mini microblogging web application.  
Implements a Java Servlet–based web app where users can create, edit, and delete short text logs with database persistence.

---

## Project Overview

This project is a Java web application built with:

- Java (Servlets)
- Maven (webapp)
- Apache Tomcat 9
- MySQL (via JDBC)
- JSP (for a simple landing page)

The application is called **Loggy** and provides basic microblogging / journaling functionality.

### Features

- Display a form to create a new log
- Each log has:
  - ID
  - Title (max 60 characters)
  - Content (max 120 characters)
  - Timestamp of creation
- List all logs ordered by most recent first
- Edit existing logs
- Delete logs
- Input validation (required fields, length limits)
- Persistent storage using JDBC and MySQL
- Thread-safe servlet design
- Clean light-themed user interface

---

## Technology Stack

- Java SE
- Java Servlet API
- Maven
- Apache Tomcat 9
- MySQL
- JDBC

---

## Repository Structure

Key files and directories:

- `pom.xml` – Maven project configuration
- `src/main/java/ca/cfrayne/loggy/model/TextLog.java`  
  Represents a single log entry (id, title, content, timestamp)
- `src/main/java/ca/cfrayne/loggy/service/JdbcLogService.java`  
  Provides CRUD operations using JDBC
- `src/main/java/ca/cfrayne/loggy/dao/DBUtil.java`  
  Utility class for obtaining JDBC connections
- `src/main/java/ca/cfrayne/loggy/web/LoggyServlet.java`  
  Main servlet that renders HTML and handles requests
- `src/main/webapp/index.jsp`  
  Simple landing page linking to the main Loggy servlet
- `src/main/webapp/WEB-INF/web.xml`  
  Web application deployment descriptor
- `loggy-lab4-041055519.sql`  
  Database DDL for creating the required schema and tables

Package namespace used in this project:

```
ca.cfrayne.loggy
```

---

## Requirements

To build and run this project you need:

- Java Development Kit (JDK) 8 or higher
- Apache Maven
- Apache Tomcat 9
- MySQL Server
- A modern web browser

Optional (but recommended):

- Visual Studio Code
- Extension Pack for Java
- Tomcat for Java extension

---

## Database Setup

Create the database and table using the provided DDL file:

```
loggy-lab4-041055519.sql
```

Example schema:

```sql
CREATE DATABASE IF NOT EXISTS loggydb;
USE loggydb;

CREATE TABLE IF NOT EXISTS logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(60) NOT NULL,
    content VARCHAR(120) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

---

## Environment Variables

Database credentials are **not stored in the source code**.  
They must be provided via environment variables.

Required environment variables:

```bash
LOGGY_DB_URL=jdbc:mysql://localhost:3306/loggydb?useSSL=false&serverTimezone=UTC
LOGGY_DB_USER=root
LOGGY_DB_PASSWORD=your_mysql_password
```

On Windows (PowerShell):

```powershell
setx LOGGY_DB_URL "jdbc:mysql://localhost:3306/loggydb?useSSL=false&serverTimezone=UTC"
setx LOGGY_DB_USER "root"
setx LOGGY_DB_PASSWORD "your_mysql_password"
```

Restart VS Code and Tomcat after setting these variables.

---

## How To Build The Project

From the project root (where `pom.xml` is located):

```bash
mvn clean package
```

This will generate the WAR file:

```
target/loggy-lab4-041055519.war
```

---

## How To Deploy and Run

1. Start Apache Tomcat 9.
2. Deploy the WAR file:
   - Copy `loggy-lab4-041055519.war` into Tomcat’s `webapps` directory  
   **OR**
   - Deploy using the Tomcat extension in VS Code.
3. Open a browser and navigate to:

```
http://localhost:8080/loggy-lab4-041055519/
```

4. From the landing page, click the link to open Loggy.

Main servlet mapping:

```
/loggy
```

Example full URL:

```
http://localhost:8080/loggy-lab4-041055519/loggy
```

---

## How To Use The Application

### Create a Log

1. Open the Loggy page.
2. Enter a title (max 60 characters).
3. Enter content (max 120 characters).
4. Click **Submit**.
5. The new log appears in the list below.

### Edit a Log

1. Click **Edit** next to an existing log.
2. The form becomes pre-populated.
3. Modify the fields.
4. Click **Submit** to save changes.

### Delete a Log

1. Click **Delete** next to a log.
2. The log is removed from the list and database.

---

## Thread Safety Notes

- The `LoggyServlet` is stateless and does not store shared mutable state.
- Database access is handled per request using JDBC connections.
- Connections and statements are managed with try-with-resources.
- This design allows safe concurrent access by multiple users.

---

## Assignment Deliverables

This project satisfies the assignment requirements by providing:

- A WAR file for Tomcat 9
- A database DDL file
- A Java Servlet-based web application
- JDBC-based persistence
- Thread-safe design
- A documented repository

Additional submission items include:

- Screencast video (1–2 minutes) demonstrating functionality
- DOCX report describing implementation, challenges, and solutions

---

## Git and .gitignore

The following items should not be committed:

```
target/
.env
.classpath
.project
.settings/
.idea/
```

Sensitive information such as database credentials is externalized using environment variables.

---

## License / Notes

This project was developed as part of a course assignment and is intended for educational use.
