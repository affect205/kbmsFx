package com.kbmsfx.gui.top;

import com.kbmsfx.entity.TItem;
import com.kbmsfx.enums.TreeKind;
import com.kbmsfx.gui.left.CategoryTree;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
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
public class HDragboardPanel extends TitledPane {

    private Set<MenuButton> buttonSet;

    private HBox wrap;

    public HDragboardPanel() {
        super();
        setMinHeight(50);
        setPadding(new Insets(10));
        setText("Категории");
        setExpanded(true);
        addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (isCollapsible()) setExpanded(true);
        });
        HBox.setMargin(this, new Insets(10, 0, 10, 0));
    }

    @PostConstruct
    public void init() {
        buttonSet = new HashSet<>();

        wrap = new HBox();

        MenuButton m3 = new MenuButton("Eats");
        m3.setPrefWidth(100);
        m3.setPopupSide(Side.BOTTOM);
        m3.getItems().addAll(new MenuItem("Burger"), new MenuItem("Hot Dog"), new Menu(""));
        wrap.getChildren().add(m3);

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

        setContent(wrap);
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
