package com.kbmsfx.gui.left;

import com.kbmsfx.events.RefreshTreeEvent;
import com.kbmsfx.events.SearchEvent;
import com.kbmsfx.events.SelectRequestEvent;
import javafx.geometry.Insets;
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
        setMargin(searchPanel, new Insets(10, 0, 10, 10));
        setMargin(categoryTree, new Insets(0, 0, 10, 10));
    }

    public void refreshTree(@Observes RefreshTreeEvent event) {
        System.out.println("refreshTree...");
        categoryTree.setSelectedItem(event.getSelected());
    }

    public void selectRequestTree(@Observes SelectRequestEvent event) {
        System.out.println("selectRequestTree...");
        if (event == null || event.getItem() == null) return;
        categoryTree.getSelectionModel().select(event.getItem());
    }

    public void filterTree(@Observes SearchEvent event) {
        System.out.println("filterTree...");
        if (event == null) return;
        categoryTree.filterBy(event.getFilter());
    }
}
