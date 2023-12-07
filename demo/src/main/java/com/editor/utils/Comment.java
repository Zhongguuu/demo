package com.editor.utils;

public class Comment {
    private String author;
    private String content;
    private String time;

    public Comment(String author, String content, String time) {
        this.author = author;
        this.content = content;
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}
