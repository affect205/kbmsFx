package com.kbmsfx.events;

/**
 * Created by Alex on 17.07.2016.
 */
public class UpdateEvent {
    private Class<?> updated;

    public UpdateEvent(Class<?> updated) {
        this.updated = updated;
    }

    public Class<?> getUpdated() {
        return updated;
    }
}
