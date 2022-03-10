package com.coldnight.book.beans;

public class Page {
    private int page;
    private String content;

    public Page(int page, String content) {
        this.page = page;
        this.content = content;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public String getContent() {
        return content;
    }
}
