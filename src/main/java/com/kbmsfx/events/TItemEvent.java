package com.kbmsfx.events;

import com.kbmsfx.entity.TItem;

/**
 * Created by Alex on 16.07.2016.
 */
public class TItemEvent {

    private TItem item;

    public TItemEvent(TItem item) {
        this.item = item;
    }

    public TItem getItem() {
        return item;
    }
}
