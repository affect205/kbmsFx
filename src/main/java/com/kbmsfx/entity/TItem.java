package com.kbmsfx.entity;

import com.kbmsfx.enums.TreeKind;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 13.07.16
 */
public abstract class TItem {
    protected int id;
    protected String name;
    protected TreeKind kind;

    public TItem(int id, String name, TreeKind kind) {
        this.id = id;
        this.name = name;
        this.kind = kind;
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

    public TreeKind getKind() {
        return kind;
    }

    public void setKind(TreeKind kind) {
        this.kind = kind;
    }

    public TItem toTItem() { return (TItem)this; }

    @Override
    public String toString() {
        return "TItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", kind=" + kind +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TItem)) return false;
        TItem tItem = (TItem) o;

        if (getId() != tItem.getId()) return false;
        if (!getName().equals(tItem.getName())) return false;
        return getKind() == tItem.getKind();
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getKind().hashCode();
        return result;
    }

    public boolean customEquals(TItem item) {
        if (item == null) return false;
        return id == item.getId() && kind == item.getKind();
    }
}
