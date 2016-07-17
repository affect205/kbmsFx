package com.kbmsfx.gui.component;

import com.kbmsfx.entity.Notice;
import com.kbmsfx.events.UpdateEvent;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
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
    private Separator SEPARATOR;
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
        System.out.println("create DisplayContainer " + new Date().getTime());
        SEPARATOR = new Separator(Orientation.VERTICAL);
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

        wrap = new VBox(titledPane, SEPARATOR);

        getChildren().setAll(wrap);
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
        wrap.getChildren().add(SEPARATOR);
        return wrap;
    }

    protected void updateRootPanel() {
        updateEvent.fire(new UpdateEvent(DisplayContainer.class));
    }

    protected void collapseAll() {
        noticeList.forEach(c-> c.setExpanded(false));
    }
}
