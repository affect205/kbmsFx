package com.kbmsfx.gui.center;

import com.kbmsfx.annotations.QAEvent;
import com.kbmsfx.entity.TItem;
import com.kbmsfx.enums.TreeKind;
import com.kbmsfx.events.ShowNoticeQAEvent;
import com.kbmsfx.events.RefreshQAEvent;
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

    public void showNoticeQA(@Observes ShowNoticeQAEvent event) {
        TreeItem<TItem> ti = event.getItem();
        if (ti == null) return;
        noticeQAPanel.setItem(ti);
    }

    public void refreshNoticeQA(@Observes @QAEvent(QAEvent.QAТуре.NOTICE)RefreshQAEvent event) {
        if (event == null || event.getItem() == null || event.getItem().getValue() == null || event.getItem().getValue().getKind() != TreeKind.NOTICE) return;
        System.out.printf("refreshNoticeQA: %s...\n", event.getItem().getValue().toString());
        noticeQAPanel.refreshNoticeQA(event.getItem());
    }

    public void refreshCategoryQA(@Observes @QAEvent(QAEvent.QAТуре.CATEGORY)RefreshQAEvent event) {
        if (event == null || event.getItem() == null || event.getItem().getValue() == null || event.getItem().getValue().getKind() != TreeKind.CATEGORY) return;
        System.out.println("refreshCategoryQA...");
    }
}
