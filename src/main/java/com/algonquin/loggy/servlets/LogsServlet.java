package com.algonquin.loggy.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.algonquin.loggy.beans.Log;
import com.algonquin.loggy.beans.TextLog;
// import com.algonquin.loggy.dao.ApplicationDao;
import com.algonquin.loggy.inmemory.ApplicationInMemory;
import com.algonquin.loggy.services.ApplicationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Lab 6 (MVC refactor):
 *
 * Controller = this Servlet (handles requests, talks to service/model)
 * View       = JSP (/WEB-INF/views/logs.jsp)
 * Model      = beans + service (in-memory or DAO)
 *
 * NOTE: The starter version rendered HTML directly from the Servlet using PrintWriter.
 * This refactor removes that HTML and forwards to a JSP view instead.
 */
public class LogsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final ApplicationService logs;

    public LogsServlet() {
        super();
        // For minimum solution, keep the in-memory implementation.
        // (DAO/MySQL is still provided in the starter and can be enabled if needed.)
        this.logs = new ApplicationInMemory();
        // this.logs = new ApplicationDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            delete(request);
            response.sendRedirect("logs");
            return;
        }

        // If id present, load log for editing
        String id = request.getParameter("id");
        String title = "";
        String content = "";
        if (id != null && !"".equals(id)) {
            Log log = this.logs.readLog(id);
            if (log != null) {
                title = log.getTitle();
                content = log.getContent();
            } else {
                id = "";
            }
        } else {
            id = "";
        }

        // Read all logs and sort by timestamp desc
        Map<UUID, Log> map = this.logs.readLogs();
        List<Log> list = new ArrayList<Log>(map.values());
        list.sort(new Comparator<Log>() {
            @Override
            public int compare(Log a, Log b) {
                if (a.getCreateTimestamp() == null && b.getCreateTimestamp() == null) return 0;
                if (a.getCreateTimestamp() == null) return 1;
                if (b.getCreateTimestamp() == null) return -1;
                return b.getCreateTimestamp().compareTo(a.getCreateTimestamp());
            }
        });

        // Model -> View
        request.setAttribute("logs", list);
        request.setAttribute("editId", id);
        request.setAttribute("editTitle", title);
        request.setAttribute("editContent", content);

        request.getRequestDispatcher("/WEB-INF/views/logs.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        // minimal server-side validation
        if (title == null) title = "";
        if (content == null) content = "";
        title = title.trim();
        content = content.trim();

        Log log;
        if (id == null || "".equals(id)) {
            log = new TextLog(title, content);
        } else {
            log = this.logs.readLog(id);
            if (log == null) {
                log = new TextLog(title, content);
            } else {
                log.setTitle(title);
                log.setContent(content);
            }
        }

        this.logs.createOrUpdateLog(log);

        // PRG pattern: redirect after post
        response.sendRedirect("logs");
    }

    private void delete(HttpServletRequest request) {
        String id = request.getParameter("id");
        if (id != null && !"".equals(id)) {
            this.logs.deleteLog(id);
        }
    }
}
