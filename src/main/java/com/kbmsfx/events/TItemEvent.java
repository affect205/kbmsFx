package com.kbmsfx.events;

import com.kbmsfx.entity.TItem;
import javafx.scene.control.TreeItem;

/**
 * Created by Alex Balyschev on 16.07.2016.
 */
public class TItemEvent {

    private TreeItem<TItem> ti;

    public TItemEvent(TreeItem<TItem> ti) {
        this.ti = ti;
    }

    public TreeItem<TItem> getItem() {
        return ti;
    }
}
