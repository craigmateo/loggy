# Loggy – Java Web Application

Loggy is a simple microblog-style Java web application developed to demo **Web Applications and Databases in Java**.

The application allows users to create, view, edit, and delete short text-based logs through a web interface, similar to a lightweight journaling or microblogging system.

---

## 🎯 Objectives Covered

This project demonstrates:

- Java object-oriented design
- Thread-safe Java Servlet programming
- Use of the Servlet lifecycle (GET/POST)
- Server-side web application development with Apache Tomcat
- In-memory data storage (Part I)
- (Planned) JDBC-based data persistence (Part II)

---

## 📦 Features (Current – Part I)

- Create a new log with:
  - Title (max 60 characters)
  - Content (max 120 characters)
- View all logs sorted by most recent first
- Edit existing logs
- Delete logs
- Thread-safe in-memory data storage
- Dynamic HTML generation using a Java Servlet

---

## 🧱 Technology Stack

- **Java (JDK 11+)**
- **Java Servlets (javax.servlet-api 4.0.1)**
- **Apache Tomcat 9**
- **Apache Maven**
- **VS Code**
- **Git & GitHub**

---

## 🗂️ Project Structure

```text
loggy/
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── ca
│   │   │       └── cfrayne
│   │   │           └── loggy
│   │   │               ├── model
│   │   │               │   └── TextLog.java
│   │   │               ├── service
│   │   │               │   └── InMemoryLogService.java
│   │   │               └── web
│   │   │                   └── LoggyServlet.java
│   │   └── webapp
│   │       ├── index.jsp
│   │       └── WEB-INF
│   │           └── web.xml
├── target
│   └── loggy-lab4-041055519.war
