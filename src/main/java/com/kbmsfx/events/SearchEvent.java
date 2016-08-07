package com.kbmsfx.events;

/**
 * Created by Alex on 07.08.2016.
 */
public class SearchEvent {

    String filter;

    public SearchEvent(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }
}
