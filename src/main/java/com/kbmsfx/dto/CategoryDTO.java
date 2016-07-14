package com.kbmsfx.dto;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 13.07.16
 */
public class CategoryDTO {

    private int id;
    private String name;
    private int parent;
    private int sorting;

    public CategoryDTO(int id, String name, int parent, int sorting) {
        this.id = id;
        this.name = name;
        this.parent = parent;
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

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
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
        return "CategoryDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parent=" + parent +
                ", sorting=" + sorting +
                '}';
    }
}
