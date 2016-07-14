package com.kbmsfx.entity;

import com.kbmsfx.enums.TreeKind;
import com.kbmsfx.utils.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 12.07.16
 */
public class Notice extends TItem {

    private String content;
    private Category category;
    private int sorting;

    public Notice(int id, String name, String content, Category category, int sorting) {
        super(id, name, TreeKind.NOTICE);
        this.id = id;
        this.name = name;
        this.content = content;
        this.category = category;
        this.sorting = sorting;
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

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getSorting() {
        return sorting;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", kind=" + kind +
                ", content='" + StringUtils.substring(content, 20) + "...'" +
                ", category=" + (category != null ? category.getId() : "null") +
                ", sorting=" + sorting +
                '}';
    }
}
