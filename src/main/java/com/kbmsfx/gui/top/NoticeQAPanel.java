package com.kbmsfx.gui.top;

import com.kbmsfx.entity.TItem;
import com.kbmsfx.events.SelectRequestEvent;
import com.kbmsfx.utils.CacheData;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alex on 15.07.2016.
 */
@Dependent
public class NoticeQAPanel extends TitledPane {

    @Inject
    private CacheData dataProvider;

    @Inject
    private Event<SelectRequestEvent> selectRequestEvent;

    private FlowPane wrap;
    private Map<Object, MenuButton> noticeQAButtons;

    public NoticeQAPanel() {
        super();
        noticeQAButtons = new HashMap<>();
        setPadding(new Insets(10));
        setText("Записи");
        setExpanded(true);
        addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (isCollapsible()) setExpanded(true);
        });
    }

    @PostConstruct
    public void init() {
        wrap = new FlowPane();
        wrap.setPrefHeight(130);
        wrap.setAlignment(Pos.TOP_LEFT);
        wrap.setOrientation(Orientation.HORIZONTAL);
        setContent(wrap);
    }

    public void setItem(TreeItem<TItem> ti) {
        System.out.println("NoticeQAPanel: setItem...");
        dataProvider.addNoticeQACache(ti);
        refreshPanel();
    }

    public void refreshPanel() {
        wrap.getChildren().clear();
        noticeQAButtons.clear();
        dataProvider.getNoticeQACache().forEach(ti -> {
            TItem item = ti.getValue();
            MenuButton noticeMB = new MenuButton(item.getName());
            noticeMB.setUserData(ti);
            noticeMB.setTooltip(new Tooltip(item.getName()));
            noticeMB.setMaxWidth(160);
            noticeMB.setPopupSide(Side.BOTTOM);
            noticeMB.getItems().addAll(buildViewMI(noticeMB), buildCloseMI(noticeMB));
            wrap.getChildren().add(noticeMB);
            wrap.setMargin(noticeMB, new Insets(5, 0, 0, 0));
            noticeQAButtons.put(item.getId(), noticeMB);
        });
    }

    public void refreshNoticeQA(TreeItem<TItem> ti) {
        TItem updated = dataProvider.updateNoticeQACache(ti);
        if (updated != null && noticeQAButtons.get(updated.getId()) != null) {
            noticeQAButtons.get(updated.getId()).setText(updated.getName());
            noticeQAButtons.get(updated.getId()).setTooltip(new Tooltip(updated.getName()));
        }
    }

    protected MenuItem buildCloseMI(MenuButton noticeMB) {
        MenuItem closeMI = new MenuItem("X Закрыть");
        closeMI.setOnAction(event -> {
            TreeItem<TItem> ti = (TreeItem<TItem>)noticeMB.getUserData();
            if (ti == null || ti.getValue() == null) return;
            dataProvider.removeNoticeQACache(ti.getValue().getId());
            refreshPanel();
        });
        return closeMI;
    }

    protected MenuItem buildViewMI(MenuButton noticeMB) {
        MenuItem viewMI = new MenuItem("V Показать");
        viewMI.setOnAction(event -> {
            TreeItem<TItem> ti = (TreeItem<TItem>)noticeMB.getUserData();
            System.out.printf("noticeQA clicked %s\n", ti);
            selectRequestEvent.fire(new SelectRequestEvent(ti));
        });
        return viewMI;
    }
}
