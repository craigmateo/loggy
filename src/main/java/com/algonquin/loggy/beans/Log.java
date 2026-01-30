package com.algonquin.loggy.beans;

import java.util.Date;
import java.util.UUID;

public abstract class Log {
    private UUID id;
    private String title;
    private String content;
    private Date createTimestamp;

    public Log() {
    }

    public Log(String title, String content) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.createTimestamp = new Date();
    }

    /**
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the createTimestamp
     */
    public Date getCreateTimestamp() {
        return createTimestamp;
    }

    /**
     * @param createTimestamp the createTimestamp to set
     */
    public void setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

}
