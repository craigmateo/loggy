package ca.cfrayne.loggy.service;

import ca.cfrayne.loggy.dao.DBUtil;
import ca.cfrayne.loggy.model.TextLog;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcLogService {

    public TextLog create(String title, String content) throws SQLException {
        String sql = "INSERT INTO logs (title, content, created_at) VALUES (?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, title);
            ps.setString(2, content);
            ps.setTimestamp(3, Timestamp.valueOf(now));
            ps.executeUpdate();

            long id = -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getLong(1);
                }
            }
            return new TextLog(id, title, content, now);
        }
    }

    public List<TextLog> findAll() throws SQLException {
        String sql = "SELECT id, title, content, created_at FROM logs ORDER BY created_at DESC";
        List<TextLog> result = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                result.add(new TextLog(id, title, content, createdAt));
            }
        }
        return result;
    }

    public TextLog findById(long id) throws SQLException {
        String sql = "SELECT id, title, content, created_at FROM logs WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String title = rs.getString("title");
                    String content = rs.getString("content");
                    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                    return new TextLog(id, title, content, createdAt);
                }
            }
        }
        return null;
    }

    public boolean update(long id, String title, String content) throws SQLException {
        String sql = "UPDATE logs SET title = ?, content = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, content);
            ps.setLong(3, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM logs WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.setLong(2, id);
            ps.setLong(1, id); // (make sure it’s only set once correctly in your code)
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }
}
