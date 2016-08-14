package com.kbmsfx.events;

import com.kbmsfx.entity.TItem;
import javafx.scene.control.TreeItem;

/**
 * Created by Alex on 14.08.2016.
 */
public class RefreshAllCategoryQAEvent {

    TreeItem<TItem> item;

    public RefreshAllCategoryQAEvent(TreeItem<TItem> item) {
        if (item == null || item.getValue() == null) return;
        this.item = item;
    }

    public TreeItem<TItem> getItem() {
        return item;
    }
}
