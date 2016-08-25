package com.kbmsfx.gui.left;

import com.kbmsfx.enums.IconKind;
import com.kbmsfx.events.SearchEvent;
import com.kbmsfx.utils.GuiUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * Created by Alex on 27.07.2016.
 */
@Dependent
public class SearchPanel extends HBox {

    @Inject
    private Event<SearchEvent> searchTreeEvent;

    private TextField nameTf;
    private Button searchBtn;

    public SearchPanel() {
        super();
    }

    @PostConstruct
    public void init() {
        nameTf = new TextField();
        nameTf.setMinHeight(28);
        nameTf.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                searchBtn.fire();
            }
        });
        searchBtn = new Button("", GuiUtils.buildIcon(IconKind.SEARCH));
        searchBtn.setTooltip(new Tooltip("Начать поиск"));
        searchBtn.setOnAction(event -> {
            searchTreeEvent.fire(new SearchEvent(nameTf.getText().trim()));
        });
        HBox.setHgrow(nameTf, Priority.ALWAYS);
        VBox.setMargin(this, new Insets(10, 0, 10, 0));
        getChildren().setAll(nameTf, searchBtn);
    }
}
