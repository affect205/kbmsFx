package com.kbmsfx.gui.center;

import com.kbmsfx.entity.TItem;
import com.kbmsfx.events.NoticeQAEvent;
import com.kbmsfx.events.TItemEvent;
import com.kbmsfx.gui.right.NoticeQAPanel;
import com.kbmsfx.gui.top.CategoryQAPanel;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Alex on 17.07.2016.
 */
@Singleton
public class CenterPanel extends BorderPane {

    @Inject
    private DisplayContainer displayContainer;

    @Inject
    private CategoryQAPanel categoryQAPanel;

    @Inject
    private NoticeQAPanel noticeQAPanel;

    public CenterPanel() {
        super();
    }

    @PostConstruct
    protected void init() {
        setCenter(displayContainer);
        setRight(noticeQAPanel);
        setTop(categoryQAPanel);
    }

    public void itemSelected(@Observes TItemEvent event) {
        TreeItem<TItem> ti = event.getItem();
        if (ti == null) return;
        System.out.println(String.format("Item selected - %s....", ti.getValue().toString()));
        displayContainer.setItem(ti);
    }

    public void updateNoticeQA(@Observes NoticeQAEvent event) {
        TreeItem<TItem> ti = event.getItem();
        if (ti == null) return;
        noticeQAPanel.setItem(ti);
    }
}
