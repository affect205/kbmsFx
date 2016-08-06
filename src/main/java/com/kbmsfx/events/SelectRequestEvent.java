package com.kbmsfx.events;

import com.kbmsfx.entity.TItem;
import javafx.scene.control.TreeItem;

/**
 * Created by Alex on 06.08.2016.
 */
public class SelectRequestEvent {

    private TreeItem<TItem> ti;

    public SelectRequestEvent(TreeItem<TItem> ti) {
        this.ti = ti;
    }

    public TreeItem<TItem> getItem() {
        return ti;
    }
}
