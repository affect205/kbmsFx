package com.kbmsfx.gui.component;

import com.kbmsfx.events.NoticeEvent;
import com.kbmsfx.events.UpdateEvent;
import javafx.scene.layout.BorderPane;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Alex on 17.07.2016.
 */
@Singleton
public class MainCenterPanel extends BorderPane {

    @Inject
    private DisplayContainer displayContainer;

    @Inject
    private HDragboardPanel hDragboardPanel;

    @Inject
    private VDragboardPanel vDragboardPanel;

    public MainCenterPanel() {
        super();
    }

    @PostConstruct
    protected void init() {
        setCenter(displayContainer);
        setRight(vDragboardPanel);
        setTop(hDragboardPanel);
    }

    public void noticeSelected(@Observes NoticeEvent event) {
        System.out.println(String.format("noticeSelected - id: %s....", event.getNotice().getId()));
        displayContainer.setNotice(event.getNotice());
    }

    public void updatePanel(@Observes UpdateEvent event) {
        if (DisplayContainer.class == event.getUpdated()) {
            setCenter(displayContainer);
        }
    }
}
