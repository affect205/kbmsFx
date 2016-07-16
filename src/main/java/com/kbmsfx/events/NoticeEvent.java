package com.kbmsfx.events;

import com.kbmsfx.entity.Notice;

/**
 * Created by Alex on 16.07.2016.
 */
public class NoticeEvent {

    private Notice notice;

    public NoticeEvent(Notice notice) {
        this.notice = notice;
    }

    public Notice getNotice() {
        return notice;
    }
}
