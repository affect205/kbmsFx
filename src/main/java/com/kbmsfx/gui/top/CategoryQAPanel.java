package com.kbmsfx.gui.top;

import com.kbmsfx.entity.TItem;
import com.kbmsfx.enums.TreeKind;
import com.kbmsfx.events.SelectRequestEvent;
import com.kbmsfx.gui.left.CategoryTree;
import com.kbmsfx.utils.CacheData;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Alex on 15.07.2016.
 */
@Dependent
public class CategoryQAPanel extends TitledPane {

    @Inject
    private CacheData dataProvider;

    @Inject
    Event<SelectRequestEvent> selectRequestEvent;

    private Map<Object, MenuButton> categoryQAButtons;
    private FlowPane wrap;

    public CategoryQAPanel() {
        super();
        setPadding(new Insets(10));
        setText("Категории");
        setExpanded(true);
        addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (isCollapsible()) setExpanded(true);
        });
    }

    @PostConstruct
    public void init() {
        categoryQAButtons = new HashMap<>();

        wrap = new FlowPane();
        wrap.setPrefHeight(130);
        wrap.setAlignment(Pos.TOP_LEFT);
        wrap.setOrientation(Orientation.HORIZONTAL);
        wrap.setOnDragDetected(event -> {
            event.consume();
        });
        wrap.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
        wrap.setOnDragDropped(event -> {
            boolean success = false;
            if (event.getGestureSource().getClass() == CategoryTree.class) {
                CategoryTree tree = (CategoryTree) event.getGestureSource();
                TreeItem<TItem> item =  (TreeItem<TItem>)tree.getSelectionModel().getSelectedItem();
                if (item != null && item.getValue() != null && item.getValue().getKind() == TreeKind.CATEGORY) {
                    dropTItem(item);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        setContent(wrap);
    }

    protected void dropTItem(TreeItem<TItem> ti) {
        if (ti == null || ti.getValue() == null) return;
        int code = dataProvider.addCategoryQACache(ti);
        if (code > -1) {
            TItem item = ti.getValue();
            MenuButton categoryMB = buildCategoryMB(ti);
            if (categoryMB == null) return;
            categoryQAButtons.put(item.getId(), categoryMB);
            refreshPanel(item.getId());
            System.out.printf("Dropped category: %s\n", ti.getValue().toString());
        }
    }

    protected List<MenuItem> buildPath(TreeItem<TItem> current) {
        List<MenuItem> children = new LinkedList<>();
        current.getChildren().forEach(ti -> {
            TItem child = ti.getValue();
            MenuItem item = child.getKind() == TreeKind.CATEGORY ?
                    new Menu(child.getName()) : new MenuItem(child.getName());
            item.setUserData(ti);
            if (child.getKind() == TreeKind.CATEGORY) {
                ((Menu)item).getItems().setAll(buildPath(ti));
            } else if (child.getKind() == TreeKind.NOTICE) {
                item.setOnAction(event -> {
                    System.out.printf("noticeQA from categoryQA clicked %s\n", ti);
                    selectRequestEvent.fire(new SelectRequestEvent(ti));
                });
            }
            children.add(item);
        });
        return children;
    }

    protected MenuItem buildCloseMI(MenuButton categoryMB) {
        MenuItem closeMI = new MenuItem("X Закрыть");
        closeMI.setOnAction(event -> {
            TreeItem<TItem> ti = (TreeItem<TItem>)categoryMB.getUserData();
            if (ti == null || ti.getValue() == null) return;
            dataProvider.removeCategoryQACache(ti.getValue().getId());
            wrap.getChildren().remove(categoryMB);
            categoryQAButtons.remove(categoryMB);
        });
        return closeMI;
    }

    protected MenuButton buildCategoryMB(TreeItem<TItem> ti) {
        if (ti == null || ti.getValue() == null) return null;
        MenuButton categoryMB = new MenuButton(ti.getValue().getName());
        categoryMB.setTooltip(new Tooltip(ti.getValue().getName()));
        categoryMB.setMaxWidth(160);
        categoryMB.setPopupSide(Side.BOTTOM);
        categoryMB.setUserData(ti);
        categoryMB.getItems().setAll(buildPath(ti));
        categoryMB.getItems().add(buildCloseMI(categoryMB));
        return categoryMB;
    }

    protected void refreshPanel(Object addedKey) {
        Deque<TreeItem<TItem>> categoryQACache = dataProvider.getCategoryQACache();
        Set<Object> removedKeys = new HashSet<>();
        Set<Object> cacheKeys = categoryQACache.stream().map(ti -> ti.getValue().getId()).collect(Collectors.toSet());
        categoryQAButtons.keySet().forEach(key -> {
            if (!cacheKeys.contains(key)) removedKeys.add(key);
        });
        removedKeys.forEach(key -> {
            wrap.getChildren().remove(categoryQAButtons.get(key));
            categoryQAButtons.remove(key);
        });
        MenuButton addedMB = categoryQAButtons.get(addedKey);
        wrap.getChildren().remove(addedMB);
        wrap.getChildren().add(addedMB);
        wrap.setMargin(addedMB, new Insets(5, 0, 0, 0));
    }

    public void refreshCategoryQA(TreeItem<TItem> ti) {
        if (ti == null || ti.getValue() == null) return;
        Set<TreeItem<TItem>> updated = dataProvider.updateCategoryQACache(ti);
        for (TreeItem<TItem> updatedTi : updated) {
            if (categoryQAButtons.containsKey(updatedTi.getValue().getId())) {
                MenuButton categoryMB = buildCategoryMB(updatedTi);
                if (categoryMB != null) {
                    MenuButton prevMB = categoryQAButtons.get(updatedTi.getValue().getId());
                    int ndx = wrap.getChildren().indexOf(prevMB);
                    if (ndx > -1) {
                        wrap.getChildren().set(ndx, categoryMB);
                        wrap.setMargin(categoryMB, new Insets(5, 0, 0, 0));
                        categoryQAButtons.put(updatedTi.getValue().getId(), categoryMB);
                    }
                }
            }
        }
    }
}
