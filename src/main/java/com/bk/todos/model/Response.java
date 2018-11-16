package com.bk.todos.model;

public enum Response {
    SUCCESS("success");

    private final String content;

    Response(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}