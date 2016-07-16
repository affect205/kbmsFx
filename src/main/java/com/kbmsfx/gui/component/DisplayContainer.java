package com.kbmsfx.gui.component;

import com.kbmsfx.entity.Notice;
import com.kbmsfx.events.NoticeEvent;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Alex on 16.07.2016.
 */
@Dependent
public class DisplayContainer extends VBox {

    private List<TitledPane> noticeSet;
    private Separator SEPARATOR;
    private BorderPane parent;
    private VBox wrap;

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
        SEPARATOR = new Separator(Orientation.VERTICAL);
    }

    @PostConstruct
    protected void init() {
        noticeSet = new LinkedList<>();

        HTMLEditor htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(600);
        htmlEditor.setHtmlText(INITIAL_TEXT);
        TitledPane titledPane = new TitledPane("HTML editor", htmlEditor);
        titledPane.setId("n.0");
        noticeSet.add(titledPane);

        wrap = new VBox(titledPane, SEPARATOR);

        getChildren().setAll(wrap);
    }

    public void noticeSelected(@Observes NoticeEvent event) {
        Notice notice = event.getNotice();
        System.out.println(String.format("noticeSelected - id: %s....", event.getNotice().getId()));
        TitledPane titledPane = buildEditPanel(notice);
        noticeSet = noticeSet.stream()
                .filter(n -> !n.getId().equals(notice.getId()))
                .collect(Collectors.toList());
        noticeSet.add(titledPane);

        getChildren().setAll(buildWrap());
        if (parent != null) parent.setCenter(this);
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
        wrap.getChildren().setAll(noticeSet);
        wrap.getChildren().add(SEPARATOR);
        return wrap;
    }

    public void setParent(BorderPane parent) {
        this.parent = parent;
    }
}
