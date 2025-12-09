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


    private void renderPage(PrintWriter out, String message,
                            TextLog logToEdit, List<TextLog> allLogs) {
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Loggy</title>");
        out.println("<meta charset='UTF-8'>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; max-width: 800px; margin: auto; }");
        out.println(".msg { padding: 8px; margin: 10px 0; background: #eef; border: 1px solid #99c; }");
        out.println(".log { border-bottom: 1px solid #ccc; padding: 8px 0; }");
        out.println("a { text-decoration: none; }");
        out.println("</style>");
        out.println("</head><body>");

        out.println("<h1>Loggy - Microblog</h1>");

        if (message != null) {
            out.println("<div class='msg'>" + escape(message) + "</div>");
        }

        out.println("<h2>" + (logToEdit == null ? "Create new log"
                                                : "Edit log #" + logToEdit.getId()) + "</h2>");
        out.println("<form method='post' action='loggy'>");
        if (logToEdit != null) {
            out.println("<input type='hidden' name='id' value='" + logToEdit.getId() + "'/>");
        }
        out.println("Title (max 60):<br/>");
        out.println("<input type='text' name='title' maxlength='60' style='width:100%' value='" +
                (logToEdit != null ? escape(logToEdit.getTitle()) : "") + "'/><br/><br/>");

        out.println("Content (max 120):<br/>");
        out.println("<textarea name='content' maxlength='120' rows='3' style='width:100%'>");
        if (logToEdit != null) {
            out.print(escape(logToEdit.getContent()));
        }
        out.println("</textarea><br/><br/>");

        out.println("<button type='submit'>Submit</button>");
        out.println("</form>");

        out.println("<h2>All logs</h2>");
        if (allLogs.isEmpty()) {
            out.println("<p>No logs yet.</p>");
        } else {
            for (TextLog log : allLogs) {
                out.println("<div class='log'>");
                out.println("<strong>" + escape(log.getTitle()) + "</strong><br/>");
                out.println("<small>Created at: " + log.getCreatedAt() + "</small><br/>");
                out.println("<p>" + escape(log.getContent()) + "</p>");
                out.println("<a href='loggy?action=edit&id=" + log.getId() + "'>Edit</a> | ");
                out.println("<a href='loggy?action=delete&id=" + log.getId() + "'>Delete</a>");
                out.println("</div>");
            }
        }

        out.println("</body></html>");
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
