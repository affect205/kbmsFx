package com.kbmsfx.events;

import com.kbmsfx.entity.TItem;

/**
 * Created by Alex on 27.07.2016.
 */
public class SelectedEvent {
    private TItem item;

    public SelectedEvent(TItem item) {
        this.item =  item;
    }

    public TItem getItem() {
        return item;
    }
}
