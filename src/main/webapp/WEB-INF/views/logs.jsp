<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.algonquin.loggy.beans.Log" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Loggy - Lab 6</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/style.css">
</head>
<body>
  <div class="container">
    <h1>Loggy</h1>
    <p class="subtitle">Lab 6 – MVC refactor (Servlet Controller + JSP View)</p>

    <%
      String editId = (String)request.getAttribute("editId");
      String editTitle = (String)request.getAttribute("editTitle");
      String editContent = (String)request.getAttribute("editContent");
      if (editId == null) editId = "";
      if (editTitle == null) editTitle = "";
      if (editContent == null) editContent = "";
    %>

    <div class="card">
      <h2><%= ("".equals(editId) ? "Create a Log" : "Edit Log") %></h2>
      <form action="logs" method="post">
        <input type="hidden" name="id" value="<%= editId %>"/>

        <label>Title (max ~60 chars)</label>
        <input name="title" maxlength="60" required value="<%= editTitle %>"/>

        <label>Description (max ~120 chars)</label>
        <textarea name="content" maxlength="120" rows="3" required><%= editContent %></textarea>

        <button type="submit"><%= ("".equals(editId) ? "Post" : "Update") %></button>
        <% if (!"".equals(editId)) { %>
          <a class="link" href="logs">Cancel</a>
        <% } %>
      </form>
    </div>

    <h2>Recent Logs</h2>

    <%
      List<Log> logs = (List<Log>)request.getAttribute("logs");
      if (logs == null || logs.isEmpty()) {
    %>
        <p class="empty">No logs yet. Add one above.</p>
    <%
      } else {
        for (Log log : logs) {
    %>
      <div class="post">
        <div class="post-header">
          <div>
            <div class="post-title"><%= log.getTitle() %></div>
            <div class="post-meta">
              <%= (log.getCreateTimestamp() == null ? "" : log.getCreateTimestamp().toString()) %>
            </div>
          </div>
          <div class="post-actions">
            <a href="logs?id=<%= log.getId() %>">edit</a>
            <a href="logs?action=delete&id=<%= log.getId() %>" onclick="return confirm('Delete this log?');">delete</a>
          </div>
        </div>

        <div class="post-content"><%= log.getContent() %></div>

        <!-- Thumbnail placeholder (minimum requirement). In a full version this would show media thumbnail. -->
        <div class="thumb">[thumbnail]</div>
      </div>
    <%
        }
      }
    %>
  </div>
</body>
</html>
