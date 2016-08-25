package com.kbmsfx.events;

import com.kbmsfx.entity.TItem;

/**
 * Created by Alex Balyschev on 30.07.2016.
 */
public class RefreshTreeEvent {
    private TItem selected;

    public RefreshTreeEvent(TItem selected) {
        this.selected = selected;
    }

    public TItem getSelected() {
        return selected;
    }
}
