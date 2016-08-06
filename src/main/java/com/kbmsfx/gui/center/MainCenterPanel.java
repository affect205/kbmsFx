package com.kbmsfx.gui.center;

import com.kbmsfx.entity.TItem;
import com.kbmsfx.events.TItemEvent;
import com.kbmsfx.gui.right.VDragboardPanel;
import com.kbmsfx.gui.top.HDragboardPanel;
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

    public void itemSelected(@Observes TItemEvent event) {
        TItem item = event.getItem();
        if (item == null) return;
        System.out.println(String.format("Item selected - %s....", item.toString()));
        displayContainer.setItem(item);
    }
}
