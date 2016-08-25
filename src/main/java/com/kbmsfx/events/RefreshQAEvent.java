package com.kbmsfx.events;

import com.kbmsfx.entity.TItem;
import javafx.scene.control.TreeItem;

/**
 * Created by Alex Balyschev on 08.08.2016.
 */
public class RefreshQAEvent {

    private TreeItem<TItem> item;

    public RefreshQAEvent(TreeItem<TItem> item) {
        this.item = item;
    }

    public TreeItem<TItem> getItem() {
        return item;
    }
}
