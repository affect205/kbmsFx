package com.kbmsfx.gui.center;

import com.kbmsfx.annotations.QAEvent;
import com.kbmsfx.entity.Notice;
import com.kbmsfx.entity.TItem;
import com.kbmsfx.enums.TreeKind;
import com.kbmsfx.events.ShowNoticeQAEvent;
import com.kbmsfx.events.RefreshQAEvent;
import com.kbmsfx.events.RefreshTreeEvent;
import com.kbmsfx.utils.CacheData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Date;

/**
 * Created by Alex on 16.07.2016.
 */
@Dependent
public class DisplayContainer extends VBox {

    private VBox wrap;
    private TextField nameTf;
    private HTMLEditor contentHE;
    private TitledPane titledPane;
    private HBox toolbar;

    private TreeItem<TItem> currentTI;

    @Inject
    private Event<RefreshTreeEvent> refreshTreeEvent;

    @Inject
    private Event<ShowNoticeQAEvent> noticeQAEvent;

    @Inject @QAEvent(QAEvent.QAТуре.CATEGORY)
    private Event<RefreshQAEvent> refreshCQAEvent;

    @Inject @QAEvent(QAEvent.QAТуре.NOTICE)
    private Event<RefreshQAEvent> refreshNQAEvent;

    @Inject
    private CacheData dataProvider;

    public DisplayContainer() {
        super();
        setAlignment(Pos.TOP_CENTER);
        System.out.println("create DisplayContainer " + new Date().getTime());
    }

    @PostConstruct
    protected void init() {

        toolbar = buildToolbar();

        nameTf = new TextField("");

        contentHE = new HTMLEditor();
        contentHE.setPrefHeight(600);
        contentHE.setHtmlText("");

        wrap = new VBox();
        wrap.getChildren().setAll(nameTf, contentHE);
        VBox.setMargin(nameTf, new Insets(0, 0, 10, 0));

        titledPane = new TitledPane("Просмотр", wrap);
        titledPane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (titledPane.isCollapsible()) titledPane.setExpanded(true);
        });

        setMargin(titledPane, new Insets(10, 10, 0, 10));

        getChildren().add(titledPane);
        getChildren().add(buildToolbar());
    }

    public void setItem(TreeItem<TItem> ti) {
        if (ti == null || ti.getValue() == null) return;
        currentTI = ti;
        TItem currentItem = ti.getValue();
        nameTf.setText(currentItem.getName());
        if (currentItem.getKind() == TreeKind.NOTICE) {
            Notice notice = (Notice)currentItem;
            contentHE.setHtmlText(notice.getContent());
            contentHE.setDisable(false);
            noticeQAEvent.fire(new ShowNoticeQAEvent(ti));
        } else {
            contentHE.setHtmlText("");
            contentHE.setDisable(true);
        }
    }

    protected HBox buildToolbar() {
        Button saveBtn = new Button("Сохранить");
        saveBtn.setOnAction(actionEvent -> {
            saveData();
        });
        saveBtn.setPrefWidth(120);
        HBox topToolbar = new HBox(saveBtn);
        topToolbar.setAlignment(Pos.BASELINE_RIGHT);
        VBox.setMargin(topToolbar, new Insets(10, 10, 10, 0));
        return topToolbar;
    }

    public void saveData() {
        System.out.println("Save data...");
        if (currentTI == null || currentTI.getValue() == null) return;
        currentTI.getValue().setName(nameTf.getText().trim());
        if (currentTI.getValue().getKind() == TreeKind.NOTICE) {
            ((Notice)currentTI.getValue()).setContent(contentHE.getHtmlText());
        }
        dataProvider.editTreeItem(currentTI.getValue());
        refreshTreeEvent.fire(new RefreshTreeEvent(currentTI.getValue()));
        if (currentTI.getValue().getKind() == TreeKind.CATEGORY) {
            refreshCQAEvent.fire(new RefreshQAEvent(currentTI));
        } else if (currentTI.getValue().getKind() == TreeKind.NOTICE) {
            refreshNQAEvent.fire(new RefreshQAEvent(currentTI));
        }
    }
}