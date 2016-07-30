package com.kbmsfx.gui.component;

import com.kbmsfx.events.RefreshTreeEvent;
import com.kbmsfx.events.SelectedEvent;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Alex on 27.07.2016.
 */
@Singleton
public class LeftPanel extends VBox {

    @Inject
    CategoryTree categoryTree;

    @Inject
    TreeEditPanel treeEditPanel;

    public LeftPanel() {
        super();
    }

    @PostConstruct
    protected void init() {
        getChildren().setAll(treeEditPanel, categoryTree);
    }

    public void selectedItem(@Observes SelectedEvent event) {
        System.out.println("selected event...");
        if (event != null) {
            treeEditPanel.setSelectedItem(event.getItem());
        }
    }

    public void refreshTree(@Observes RefreshTreeEvent event) {
        System.out.println("refreshTree...");
        categoryTree.setSelectedItem(event.getSelected());
    }
}
