package ca.cfrayne.loggy.web;

import ca.cfrayne.loggy.model.TextLog;
import ca.cfrayne.loggy.service.JdbcLogService;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/loggy")
public class LoggyServlet extends HttpServlet {

    private final JdbcLogService logService = new JdbcLogService();

    @Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    req.setCharacterEncoding("UTF-8");
    resp.setContentType("text/html;charset=UTF-8");

    String action = req.getParameter("action");
    String message = null;
    TextLog logToEdit = null;
    java.util.List<TextLog> allLogs = java.util.Collections.emptyList();

    try {
        if ("delete".equals(action)) {
            long id = Long.parseLong(req.getParameter("id"));
            boolean deleted = logService.delete(id);
            message = deleted ? "Log deleted successfully." : "Log not found.";
        } else if ("edit".equals(action)) {
            long id = Long.parseLong(req.getParameter("id"));
            logToEdit = logService.findById(id);
            if (logToEdit == null) {
                message = "Log not found for editing.";
            }
        }

        allLogs = logService.findAll();

    } catch (NumberFormatException e) {
        message = "Invalid ID.";
    } catch (java.sql.SQLException e) {
        message = "Database error: " + e.getMessage();
        e.printStackTrace();
    }

    try (PrintWriter out = resp.getWriter()) {
        renderPage(out, message, logToEdit, allLogs);
    }
}

    @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    req.setCharacterEncoding("UTF-8");
    resp.setContentType("text/html;charset=UTF-8");

    String idParam = req.getParameter("id");
    String title = req.getParameter("title");
    String content = req.getParameter("content");

    String message;
    java.util.List<TextLog> allLogs = java.util.Collections.emptyList();

    try {
        if (title == null || title.isBlank() || title.length() > 60 ||
            content == null || content.isBlank() || content.length() > 120) {
            message = "Validation error: Title (max 60) and Content (max 120) are required.";
        } else if (idParam == null || idParam.isBlank()) {
            // CREATE
            logService.create(title, content);
            message = "Log created successfully.";
        } else {
            // UPDATE
            long id = Long.parseLong(idParam);
            boolean updated = logService.update(id, title, content);
            message = updated ? "Log updated successfully."
                              : "Could not find log to update.";
        }

        allLogs = logService.findAll();

    } catch (NumberFormatException e) {
        message = "Invalid ID.";
    } catch (java.sql.SQLException e) {
        message = "Database error: " + e.getMessage();
        e.printStackTrace();
    }

    try (PrintWriter out = resp.getWriter()) {
        renderPage(out, message, null, allLogs);
    }
}

private String escape(String s) {
    if (s == null) return "";
    return s.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;");
}

    private void renderPage(PrintWriter out, String message,
                        TextLog logToEdit, List<TextLog> allLogs) {
    out.println("<!DOCTYPE html>");
    out.println("<html lang='en'>");
    out.println("<head>");
    out.println("  <meta charset='UTF-8'>");
    out.println("  <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
    out.println("  <title>Loggy - Microblog</title>");
    out.println("<style>");
out.println("* { box-sizing: border-box; }");

out.println("body {");
out.println("  margin: 0;");
out.println("  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;");
out.println("  background: #f4f6f8;");
out.println("  color: #1f2933;");
out.println("}");

out.println(".page {");
out.println("  max-width: 900px;");
out.println("  margin: 32px auto;");
out.println("  padding: 24px;");
out.println("}");

out.println("h1 {");
out.println("  margin: 0 0 4px;");
out.println("  font-size: 2rem;");
out.println("}");

out.println(".subtitle {");
out.println("  margin: 0 0 20px;");
out.println("  font-size: 0.95rem;");
out.println("  color: #5f6c7b;");
out.println("}");

out.println(".msg {");
out.println("  padding: 10px 14px;");
out.println("  margin-bottom: 18px;");
out.println("  border-radius: 6px;");
out.println("  background: #e6f4ea;");
out.println("  color: #1e4620;");
out.println("  border: 1px solid #b7dfc2;");
out.println("}");

out.println(".msg.error {");
out.println("  background: #fdecea;");
out.println("  color: #611a15;");
out.println("  border-color: #f5c6cb;");
out.println("}");

out.println(".layout {");
out.println("  display: grid;");
out.println("  grid-template-columns: 1fr 1.2fr;");
out.println("  gap: 24px;");
out.println("}");

out.println("@media (max-width: 800px) {");
out.println("  .layout { grid-template-columns: 1fr; }");
out.println("}");

out.println(".card {");
out.println("  background: #ffffff;");
out.println("  border-radius: 10px;");
out.println("  padding: 18px 20px;");
out.println("  border: 1px solid #e1e5ea;");
out.println("}");

out.println(".card h2 {");
out.println("  margin-top: 0;");
out.println("  font-size: 1.2rem;");
out.println("}");

out.println("label {");
out.println("  display: block;");
out.println("  font-size: 0.85rem;");
out.println("  font-weight: 600;");
out.println("  margin-bottom: 4px;");
out.println("}");

out.println("input[type='text'], textarea {");
out.println("  width: 100%;");
out.println("  padding: 9px 10px;");
out.println("  border-radius: 6px;");
out.println("  border: 1px solid #cbd2d9;");
out.println("  background: #ffffff;");
out.println("  color: #1f2933;");
out.println("  font-size: 0.9rem;");
out.println("}");

out.println("input[type='text']:focus, textarea:focus {");
out.println("  border-color: #2563eb;");
out.println("  outline: none;");
out.println("}");

out.println("textarea { resize: vertical; }");

out.println(".field { margin-bottom: 12px; }");

out.println(".hint {");
out.println("  font-size: 0.75rem;");
out.println("  color: #6b7280;");
out.println("  margin-top: 2px;");
out.println("}");

out.println(".actions {");
out.println("  display: flex;");
out.println("  justify-content: flex-end;");
out.println("  margin-top: 10px;");
out.println("}");

out.println("button {");
out.println("  background: #2563eb;");
out.println("  color: #ffffff;");
out.println("  border: none;");
out.println("  border-radius: 20px;");
out.println("  padding: 8px 18px;");
out.println("  font-size: 0.9rem;");
out.println("  cursor: pointer;");
out.println("}");

out.println("button:hover {");
out.println("  background: #1e4fd6;");
out.println("}");

out.println(".logs-list {");
out.println("  display: flex;");
out.println("  flex-direction: column;");
out.println("  gap: 12px;");
out.println("}");

out.println(".log-item {");
out.println("  padding-bottom: 10px;");
out.println("  border-bottom: 1px solid #e5e7eb;");
out.println("}");

out.println(".log-item:last-child { border-bottom: none; }");

out.println(".log-header {");
out.println("  display: flex;");
out.println("  justify-content: space-between;");
out.println("  margin-bottom: 4px;");
out.println("}");

out.println(".log-title {");
out.println("  font-weight: 600;");
out.println("}");

out.println(".log-meta {");
out.println("  font-size: 0.75rem;");
out.println("  color: #6b7280;");
out.println("}");

out.println(".log-content {");
out.println("  font-size: 0.9rem;");
out.println("  margin-bottom: 6px;");
out.println("  white-space: pre-wrap;");
out.println("}");

out.println(".log-actions a {");
out.println("  font-size: 0.8rem;");
out.println("  color: #2563eb;");
out.println("  text-decoration: none;");
out.println("  margin-right: 10px;");
out.println("}");

out.println(".log-actions a:hover { text-decoration: underline; }");

out.println(".empty {");
out.println("  font-style: italic;");
out.println("  color: #6b7280;");
out.println("}");
out.println("</style>");

    out.println("</head>");
    out.println("<body>");
    out.println("  <div class='page'>");
    out.println("    <header>");
    out.println("      <h1>Loggy</h1>");
    out.println("      <p class='subtitle'>A tiny microblog for your daily thoughts.</p>");
    out.println("    </header>");

    if (message != null) {
        // Treat messages containing 'error' (case-insensitive) as error style
        String cssClass = message.toLowerCase().contains("error") ? "msg error" : "msg";
        out.println("    <div class='" + cssClass + "'>" + escape(message) + "</div>");
    }

    out.println("    <div class='layout'>");

    // Left side: form
    out.println("      <section class='card'>");
    out.println("        <h2>" + (logToEdit == null ? "Create new log" : "Edit log #" + logToEdit.getId()) + "</h2>");
    out.println("        <form method='post' action='loggy'>");

    if (logToEdit != null) {
        out.println("          <input type='hidden' name='id' value='" + logToEdit.getId() + "'/>");
    }

    out.println("          <div class='field'>");
    out.println("            <label for='title'>Title</label>");
    out.println("            <input type='text' id='title' name='title' maxlength='60' " +
            "value='" + (logToEdit != null ? escape(logToEdit.getTitle()) : "") + "' required />");
    out.println("            <div class='hint'>Max 60 characters</div>");
    out.println("          </div>");

    out.println("          <div class='field'>");
    out.println("            <label for='content'>Content</label>");
    out.println("            <textarea id='content' name='content' maxlength='120' rows='3' required>");
    if (logToEdit != null) {
        out.print(escape(logToEdit.getContent()));
    }
    out.println("            </textarea>");
    out.println("            <div class='hint'>Max 120 characters</div>");
    out.println("          </div>");

    out.println("          <div class='actions'>");
    out.println("            <button type='submit'>" + (logToEdit == null ? "Post log" : "Save changes") + "</button>");
    out.println("          </div>");

    out.println("        </form>");
    out.println("      </section>");

    // Right side: logs list
    out.println("      <section class='card'>");
    out.println("        <h2>Recent logs</h2>");

    if (allLogs.isEmpty()) {
        out.println("        <p class='empty'>No logs yet. Start by posting something on the left.</p>");
    } else {
        out.println("        <div class='logs-list'>");
        for (TextLog log : allLogs) {
            out.println("          <article class='log-item'>");
            out.println("            <div class='log-header'>");
            out.println("              <div class='log-title'>" + escape(log.getTitle()) + "</div>");
            out.println("              <div class='log-meta'>" + log.getCreatedAt() + "</div>");
            out.println("            </div>");
            out.println("            <div class='log-content'>" + escape(log.getContent()) + "</div>");
            out.println("            <div class='log-actions'>");
            out.println("              <a href='loggy?action=edit&id=" + log.getId() + "'>Edit</a>");
            out.println("              <a href='loggy?action=delete&id=" + log.getId() + "'>Delete</a>");
            out.println("            </div>");
            out.println("          </article>");
        }
        out.println("        </div>");
    }

    out.println("      </section>");

    out.println("    </div>"); // .layout
    out.println("  </div>");   // .page
    out.println("</body>");
    out.println("</html>");
}

}
