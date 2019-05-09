package com.example.notepad.entity;

public class NoteVO {
    private Integer id;
    private String title;
    private String content;
    private String lastUpdate;
    private String tag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "NoteVO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
