package com.kbmsfx.gui.component;

import com.kbmsfx.entity.Notice;
import com.kbmsfx.events.UpdateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.HTMLEditor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Alex on 16.07.2016.
 */
@Dependent
public class DisplayContainer extends VBox {

    private List<TitledPane> noticeList;
    private VBox wrap;

    @Inject
    private Event<UpdateEvent> updateEvent;

    private final String INITIAL_TEXT = "Lorem ipsum dolor sit "
            + "amet, consectetur adipiscing elit. Nam tortor felis, pulvinar "
            + "in scelerisque cursus, pulvinar at ante. Nulla consequat"
            + "congue lectus in sodales. Nullam eu est a felis ornare "
            + "bibendum et nec tellus. Vivamus non metus tempus augue auctor "
            + "ornare. Duis pulvinar justo ac purus adipiscing pulvinar. "
            + "Integer congue faucibus dapibus. Integer id nisl ut elit "
            + "aliquam sagittis gravida eu dolor. Etiam sit amet ipsum "
            + "sem.";

    public DisplayContainer() {
        super();
        this.setAlignment(Pos.TOP_CENTER);
        System.out.println("create DisplayContainer " + new Date().getTime());
    }

    @PostConstruct
    protected void init() {
        noticeList = new LinkedList<>();

        HTMLEditor htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(600);
        htmlEditor.setHtmlText(INITIAL_TEXT);
        TitledPane titledPane = new TitledPane("HTML editor", htmlEditor);
        titledPane.setId("n.0");
        noticeList.add(titledPane);

        wrap = new VBox(titledPane);
        VBox.setMargin(wrap, new Insets(10, 10, 0, 10));
        HBox topToolbar = buildTopToolbar();

        getChildren().add(topToolbar);
        getChildren().add(wrap);
    }

    public void setNotice(Notice notice) {
        if (notice == null) return;
        collapseAll();
        TitledPane titledPane = buildEditPanel(notice);
        noticeList = noticeList.stream()
                .filter(n -> !n.getId().equals("n." + notice.getId()))
                .collect(Collectors.toList());
        noticeList.add(titledPane);
        Collections.reverse(noticeList);
        getChildren().setAll(buildWrap());
        updateRootPanel();
    }

    protected TitledPane buildEditPanel(Notice notice) {
        HTMLEditor htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(600);
        htmlEditor.setHtmlText(notice.getContent());
        TitledPane titledPane = new TitledPane(notice.getName(), htmlEditor);
        titledPane.setId("n." + notice.getId());
        return titledPane;
    }

    protected VBox buildWrap() {
        wrap.getChildren().clear();
        wrap.getChildren().setAll(noticeList);
        return wrap;
    }

    protected void updateRootPanel() {
        updateEvent.fire(new UpdateEvent(DisplayContainer.class));
    }

    protected void collapseAll() {
        noticeList.forEach(c-> c.setExpanded(false));
    }

    protected HBox buildTopToolbar() {
        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(actionEvent -> {
            System.out.println("Save notice...");
        });
        HBox topToolbar = new HBox(saveBtn);
        topToolbar.setAlignment(Pos.BASELINE_RIGHT);
        VBox.setMargin(topToolbar, new Insets(0, 10, 0, 10));
        return topToolbar;
    }
}
