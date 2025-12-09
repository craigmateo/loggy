# Loggy Lab 4 – 041055519

Mini microblogging web application for the "Loggy" assignment.  
Implements a Java Servlet–based web app where users can create, edit, and delete short text logs.

---

## Project Overview

This project is a simple web application built with:

- Java (Servlets)
- Maven (webapp)
- Apache Tomcat 9
- JSP (for a simple landing page)
- In-memory storage (Part I), to be extended to JDBC + Database (Part II)

Features implemented:

- Display a form to create a new log
- Each log has:
  - ID
  - Title (max 60 characters)
  - Content (max 120 characters)
  - Timestamp of creation
- List all logs ordered by most recent first
- Edit existing logs
- Delete logs
- Basic validation on input length and required fields

---

## Requirements

To build and run this project you need:

- Java Development Kit (JDK) 8 or higher
- Apache Maven
- Apache Tomcat 9
- A modern browser (Chrome, Firefox, Edge, etc.)

Optional (but helpful):

- VS Code with:
  - Extension Pack for Java
  - Tomcat for Java extension

---

## Repository Structure

Key paths:

- `pom.xml` – Maven project configuration
- `src/main/java/ca/cfrayne/loggy/model/TextLog.java`  
  Log entity with id, title, content, and timestamp
- `src/main/java/ca/cfrayne/loggy/service/InMemoryLogService.java`  
  Thread-safe in-memory storage and CRUD operations
- `src/main/java/ca/cfrayne/loggy/web/LoggyServlet.java`  
  Main servlet that renders the HTML page and handles requests
- `src/main/webapp/index.jsp`  
  Simple landing page that links to the main Loggy servlet
- `src/main/webapp/WEB-INF/web.xml`  
  Web application deployment descriptor

Note: Package path may differ slightly depending on your final package name. In this project it follows:

`ca.cfrayne.loggy`

---

## How To Set Up The Project

1. Install Java and Maven, and ensure they are on your PATH.

   - Check Java:
     - `java -version`
   - Check Maven:
     - `mvn -v`

2. Clone the repository from GitHub.

   - Example:
     - `git clone <your-repo-url>`
     - `cd loggy-lab4-041055519`

3. Build the project with Maven.

   - From the project root (where `pom.xml` is located), run:
     - `mvn clean package`

   - This will create a WAR file in:
     - `target/loggy-lab4-041055519.war`

4. Configure Apache Tomcat 9.

   - Make sure Tomcat 9 is installed.
   - Set `CATALINA_HOME` if needed.
   - Start Tomcat using:
     - `bin/startup.bat` (Windows)
     - `bin/startup.sh` (Linux / macOS)

5. Deploy the WAR to Tomcat.

   You can deploy in one of two common ways:

   - Copy the WAR file to the `webapps` folder of Tomcat:
     - Copy `target/loggy-lab4-041055519.war` to `{TOMCAT_HOME}/webapps/`
     - Tomcat will auto-extract it on startup.

   - Or, if using VS Code with the Tomcat extension:
     - Use the Tomcat panel to:
       - Add an existing Tomcat installation
       - Add this project or WAR as a web application
       - Start the server and deploy

---

## How To Run The Application

1. Ensure Tomcat is running.

2. Open your browser and navigate to the context path of the deployed app.

   Typical URL pattern:

   - `http://localhost:8080/loggy-lab4-041055519/`

3. From the landing page (`index.jsp`), click the link to go to the main Loggy page.

   The main servlet is mapped to:

   - `/loggy`

   Example full URL:

   - `http://localhost:8080/loggy-lab4-041055519/loggy`

4. You should see:

   - A page titled "Loggy - Microblog"
   - A form to create logs (Title + Content)
   - A list of logs (empty at first)

---

## How To Use The Application

### Create a new log

1. Go to the main Loggy page (`/loggy`).
2. Fill in:
   - Title (max 60 chars)
   - Content (max 120 chars)
3. Click `Submit`.
4. A success message appears, and the new log shows in the list below the form.

### Edit a log

1. In the list of logs, click the `Edit` link next to a specific log.
2. The form at the top becomes pre-populated with that log’s title and content.
3. Modify the values and click `Submit`.
4. A success message appears, and the updated log is shown in the list.

### Delete a log

1. In the list of logs, click the `Delete` link next to a specific log.
2. The log is removed from the list.
3. A confirmation message appears.

---

## Thread-Safety Notes

Servlets are by default multi-threaded: a single servlet instance may handle multiple requests concurrently. To keep this application safe:

- The servlet (`LoggyServlet`) does not store mutable request-specific state in instance fields.
- Shared data (logs) are managed in `InMemoryLogService` using:
  - `CopyOnWriteArrayList` for the internal list
  - `AtomicLong` for ID generation
- This keeps the in-memory storage simple and thread-safe for this lab exercise.

---

## Future Work (Part II – JDBC + Database)

Part II of the assignment requires:

- Creating a database and `logs` table (for example in HSQLDB or MySQL)
- Adding a data access layer using JDBC
- Replacing the `InMemoryLogService` with a `JdbcLogService` that:
  - Connects to the database
  - Performs CRUD operations using SQL
  - Maps result sets to `TextLog` objects

The steps will roughly be:

1. Create the database schema (DDL).
2. Add database driver dependency to `pom.xml`.
3. Implement a `DBUtil` class to obtain JDBC connections.
4. Implement `JdbcLogService` with create, read, update, and delete methods.
5. Update `LoggyServlet` to use `JdbcLogService` instead of `InMemoryLogService`.
6. Add the database DDL file to the project root for submission.

---

## Assignment Deliverables (Summary)

According to the assignment instructions, the final submission should include:

- A WAR file optimized for Tomcat 9 that includes source code, static resources, and dependencies.
- A DDL file describing the database schema used (or auto-generation code).
- A short MP4 screencast (1–2 minutes) demonstrating the app working in at least two browsers.
- A DOC/DOCX report describing:
  - Features implemented
  - Changes during development
  - Challenges and how they were resolved
  - Code snippets illustrating key parts (e.g., servlet, service, JDBC)

Make sure any external sources (code, tutorials, etc.) are cited properly in the report.

---

## Git and .gitignore

A typical `.gitignore` for this project should contain entries such as:

- `target/`
- `.classpath`
- `.project`
- `.settings/`
- Any IDE-specific files you do not want committed

This keeps your repository clean and avoids committing build artifacts.

---
