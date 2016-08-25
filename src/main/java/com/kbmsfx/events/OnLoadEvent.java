package com.kbmsfx.events;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Alex Balyschev on 24.08.16.
 */
public class OnLoadEvent {
    private int id;

    public OnLoadEvent() {
        id = ThreadLocalRandom.current().nextInt(1000);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OnLoadEvent that = (OnLoadEvent) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
