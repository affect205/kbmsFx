package com.kbmsfx.gui.right;

import com.kbmsfx.entity.TItem;
import com.kbmsfx.events.SelectRequestEvent;
import com.kbmsfx.utils.CacheData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    CacheData dataProvider;

    @Inject
    Event<SelectRequestEvent> selectRequestEvent;

    private VBox wrap;
    private Map<Object, MenuButton> noticeQAButtons;

    public NoticeQAPanel() {
        super();
        noticeQAButtons = new HashMap<>();

        setPrefWidth(160);
        setPadding(new Insets(10));
        setText("Записи");
        setExpanded(true);
        addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (isCollapsible()) setExpanded(true);
        });
        HBox.setMargin(this, new Insets(10, 0, 10, 0));
    }

    @PostConstruct
    public void init() {
        wrap = new VBox();
        wrap.setMinHeight(300);
        wrap.setAlignment(Pos.TOP_CENTER);

        MenuButton m = new MenuButton("Eats");
        m.setPrefWidth(120);
        m.setUserData(1);
        m.setPopupSide(Side.BOTTOM);
        m.getItems().addAll(buildCloseMI(m));

        wrap.getChildren().addAll(m);
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
            noticeMB.setPrefWidth(120);
            noticeMB.setPopupSide(Side.BOTTOM);
            noticeMB.getItems().addAll(buildViewMI(noticeMB), buildCloseMI(noticeMB));
            wrap.getChildren().add(noticeMB);
            wrap.setMargin(noticeMB, new Insets(5, 0, 0, 0));
        });
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
