package com.kbmsfx.entity;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 12.07.16
 */
public class Notice {

    private int id;
    private String name;
    private String content;
    private Category category;
    private int order;

    public Notice(int id, String name, String content, Category category, int order) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Category getCategory() {
        return category;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
