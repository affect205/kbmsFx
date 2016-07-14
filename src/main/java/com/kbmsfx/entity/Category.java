package com.kbmsfx.entity;

import com.kbmsfx.enums.TreeKind;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 12.07.16
 */
public class Category extends TItem {

    private Category parent;
    private int sorting;

    public Category(int id) {
        this(id, "");
    }

    public Category(int id, String name) {
        super(id, name, TreeKind.CATEGORY);
    }

    public Category(int id, String name, Category parent, int sorting) {
        super(id, name, TreeKind.CATEGORY);
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.sorting = sorting;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public int getSorting() {
        return sorting;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", kind=" + kind +
                ", parent=" + (parent != null ? parent.getId() : "null") +
                ", sorting=" + sorting +
                '}';
    }
}
