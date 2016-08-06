package com.kbmsfx.gui.left;

import com.kbmsfx.events.RefreshTreeEvent;
import com.kbmsfx.events.SelectedEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
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
    SearchPanel searchPanel;

    public LeftPanel() {
        super();
    }

    @PostConstruct
    protected void init() {
        getChildren().setAll(searchPanel, categoryTree);
        VBox.setVgrow(categoryTree, Priority.ALWAYS);
    }

    public void selectedItem(@Observes SelectedEvent event) {
        System.out.println("selected event...");
        if (event != null) {
            searchPanel.setSelectedItem(event.getItem());
        }
    }

    public void refreshTree(@Observes RefreshTreeEvent event) {
        System.out.println("refreshTree...");
        categoryTree.setSelectedItem(event.getSelected());
    }
}
