package com.kbmsfx.dto;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 13.07.16
 */
public class NoticeDTO {

    private int id;
    private String name;
    private String content;
    private int categoryid;
    private int sorting;

    public NoticeDTO(int id, String name, String content, int categoryid, int sorting) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.categoryid = categoryid;
        this.sorting = sorting;
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

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public int getSorting() {
        return sorting;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }

    @Override
    public String toString() {
        return "NoticeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryid=" + categoryid +
                ", sorting=" + sorting +
                '}';
    }
}
