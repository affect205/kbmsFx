package com.kbmsfx.gui.center;

import com.kbmsfx.annotations.QAEvent;
import com.kbmsfx.entity.TItem;
import com.kbmsfx.enums.TreeKind;
import com.kbmsfx.events.*;
import com.kbmsfx.gui.top.NoticeQAPanel;
import com.kbmsfx.gui.top.CategoryQAPanel;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Alex Balyschev on 17.07.2016.
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
        HBox topQA = new HBox(categoryQAPanel, noticeQAPanel);
        HBox.setHgrow(categoryQAPanel, Priority.ALWAYS);
        HBox.setHgrow(noticeQAPanel, Priority.ALWAYS);
        setBottom(topQA);
    }

    public void itemSelected(@Observes TItemEvent event) {
        TreeItem<TItem> ti = event.getItem();
        if (ti == null) return;
        System.out.println(String.format("Item selected - %s....", ti.getValue().toString()));
        displayContainer.setItem(ti);
    }

    public void showNoticeQA(@Observes ShowNoticeQAEvent event) {
        TreeItem<TItem> ti = event.getItem();
        if (ti == null) return;
        noticeQAPanel.setItem(ti);
    }

    public void refreshAllCategoryQA(@Observes RefreshAllCategoryQAEvent event) {
        TreeItem<TItem> ti = event.getItem();
        if (ti == null) return;
        categoryQAPanel.refreshAllCategoryQA();
    }

    public void refreshNoticeQA(@Observes @QAEvent(QAEvent.QAТуре.NOTICE)RefreshQAEvent event) {
        if (event == null || event.getItem() == null || event.getItem().getValue() == null || event.getItem().getValue().getKind() != TreeKind.NOTICE) return;
        System.out.printf("refreshNoticeQA: %s...\n", event.getItem().getValue().toString());
        noticeQAPanel.refreshNoticeQA(event.getItem());
    }

    public void refreshCategoryQA(@Observes @QAEvent(QAEvent.QAТуре.CATEGORY)RefreshQAEvent event) {
        if (event == null || event.getItem() == null || event.getItem().getValue() == null) return;
        System.out.printf("refreshCategoryQA: %s...\n", event.getItem().getValue().toString());
        categoryQAPanel.refreshCategoryQA(event.getItem());
    }
}
