package ca.cfrayne.loggy.service;

import ca.cfrayne.loggy.model.TextLog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryLogService {

    private final List<TextLog> logs = new CopyOnWriteArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    private static final InMemoryLogService INSTANCE = new InMemoryLogService();

    private InMemoryLogService() {}

    public static InMemoryLogService getInstance() {
        return INSTANCE;
    }

    public TextLog create(String title, String content) {
        long id = idGenerator.getAndIncrement();
        TextLog log = new TextLog(id, title, content, LocalDateTime.now());
        logs.add(log);
        return log;
    }

    public List<TextLog> findAll() {
        List<TextLog> copy = new ArrayList<>(logs);
        copy.sort(Comparator.comparing(TextLog::getCreatedAt).reversed());
        return copy;
    }

    public TextLog findById(long id) {
        for (TextLog log : logs) {
            if (log.getId() == id) {
                return log;
            }
        }
        return null;
    }

    public boolean delete(long id) {
        return logs.removeIf(l -> l.getId() == id);
    }

    public boolean update(long id, String title, String content) {
        TextLog existing = findById(id);
        if (existing == null) {
            return false;
        }
        existing.setTitle(title);
        existing.setContent(content);
        return true;
    }
}
