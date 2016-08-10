package com.kbmsfx.events;

import com.kbmsfx.entity.TItem;
import com.kbmsfx.enums.TreeKind;
import javafx.scene.control.TreeItem;

/**
 * Created by Alex on 06.08.2016.
 */
public class ShowNoticeQAEvent {

    TreeItem<TItem> item;

    public ShowNoticeQAEvent(TreeItem<TItem> item) {
        if (item.getValue() == null || item.getValue().getKind() != TreeKind.NOTICE) return;
        this.item = item;
    }

    public TreeItem<TItem> getItem() {
        return item;
    }
}
