package com.kbmsfx.entity;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 12.07.16
 */
public class Category {

    private int id;
    private String name;
    private Category parent;
    private int order;

    public Category(int id, String name, Category parent, int order) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.order = order;
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

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
