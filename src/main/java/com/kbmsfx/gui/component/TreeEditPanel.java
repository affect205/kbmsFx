package com.kbmsfx.gui.component;

import com.kbmsfx.entity.TItem;
import com.kbmsfx.events.RefreshTreeEvent;
import com.kbmsfx.utils.CacheData;
import com.kbmsfx.utils.StringUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
public class TreeEditPanel extends HBox {

    @Inject
    private CacheData dataProvider;

    @Inject
    private Event<RefreshTreeEvent> refreshTreeEvent;

    private TextField nameTf;
    private Button saveBtn;

    public TreeEditPanel() {
        super();
    }

    @PostConstruct
    public void init() {
        nameTf = new TextField();
        saveBtn = new Button("+");
        saveBtn.setTooltip(new Tooltip("Сохранить запись"));
        saveBtn.setOnAction(event -> {
            System.out.println("record saving....");
            TItem item = (TItem)nameTf.getUserData();
            if (item != null && !StringUtils.isEmpty(nameTf.getText())) {
                item.setName(nameTf.getText().trim());
                dataProvider.editTreeItem(item);
                refreshTreeEvent.fire(new RefreshTreeEvent(item));
            }
        });
        HBox.setHgrow(nameTf, Priority.ALWAYS);
        VBox.setMargin(this, new Insets(10, 0, 10, 0));
        getChildren().setAll(nameTf, saveBtn);
    }

    public void setSelectedItem(TItem item) {
        if (item == null) return;
        nameTf.setUserData(item);
        nameTf.setText(item.getName());
    }
}
