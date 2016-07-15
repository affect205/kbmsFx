package com.kbmsfx.gui.component;

import com.kbmsfx.entity.TItem;
import com.kbmsfx.enums.TreeKind;
import javafx.geometry.Side;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Alex on 15.07.2016.
 */
@Dependent
public class HDragboardPanel extends HBox {

    private Set<MenuButton> buttonSet;

    public HDragboardPanel() {
        super();
        setMinHeight(30);
    }

    @PostConstruct
    public void init() {
        buttonSet = new HashSet<>();

        MenuButton m3 = new MenuButton("Eats");
        m3.setPrefWidth(100);
        m3.setPopupSide(Side.BOTTOM);
        m3.getItems().addAll(new MenuItem("Burger"), new MenuItem("Hot Dog"), new Menu(""));


        setOnDragDetected(event -> {
            event.consume();
        });
        setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
        setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (event.getGestureSource().getClass() == CategoryTree.class) {
                CategoryTree tree = (CategoryTree) event.getGestureSource();
                TreeItem<TItem> item =  (TreeItem<TItem>)tree.getSelectionModel().getSelectedItem();
                if (item != null) {
                    dropTItem(item);
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    protected void dropTItem(TreeItem<TItem> item) {
        if (item == null) return;
        MenuButton menuBtn = new MenuButton(item.getValue().getName());
        menuBtn.setMaxWidth(160);
        menuBtn.setPopupSide(Side.BOTTOM);
        menuBtn.setUserData(item);
        menuBtn.setId(String.format("%s%s", item.getValue().getKind(), item.getValue().getId()));
        menuBtn.getItems().setAll(buildPath(item));

        getChildren().clear();
        buttonSet = buttonSet.stream()
                .filter(b -> !b.getId().equals(menuBtn.getId()))
                .collect(Collectors.toSet());
        buttonSet.add(menuBtn);
        getChildren().setAll(buttonSet);

        System.out.println(String.format("Dropped - id: %s, name: %s, kind: %s", item.getValue().getId(), item.getValue().getName(), item.getValue().getKind()));
    }


    protected List<MenuItem> buildPath(TreeItem<TItem> current) {
        List<MenuItem> children = new LinkedList<>();
        current.getChildren().forEach(c -> {
            TItem child = c.getValue();
            MenuItem item = child.getKind() == TreeKind.CATEGORY ?
                    new Menu(child.getName()) : new MenuItem(child.getName());
            item.setUserData(child);
            item.setId(String.format("%s%s", child.getKind(), child.getId()));
            if (child.getKind() == TreeKind.CATEGORY) {
                ((Menu)item).getItems().setAll(buildPath(c));
            }
            children.add(item);
        });
        return children;
    }
}