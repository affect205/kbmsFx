package com.kbmsfx.gui.component;

import com.kbmsfx.entity.Category;
import com.kbmsfx.entity.Notice;
import com.kbmsfx.entity.TItem;
import com.kbmsfx.enums.TreeKind;
import com.kbmsfx.events.NoticeEvent;
import com.kbmsfx.events.SelectedEvent;
import com.kbmsfx.utils.CacheData;
import com.kbmsfx.utils.EntityUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.util.Callback;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * Created by Alex on 15.07.2016.
 */
@Dependent
public class CategoryTree extends TreeTableView {

    @Inject
    private CacheData dataProvider;

    @Inject
    private Event<NoticeEvent> itemEvent;

    @Inject
    private Event<SelectedEvent> selectedEvent;

    private TreeItem<TItem> root;

    private MenuItem addCatMenuItem;
    private MenuItem addNotMenuItem;
    private MenuItem removeMenuItem;
    private ContextMenu contextMenu;

    public CategoryTree() {
        super();
        contextMenu = new ContextMenu();
        addCatMenuItem = new MenuItem("+ Добавить категорию");
        addCatMenuItem.setOnAction(event -> {
            TreeItem<TItem> selected = (TreeItem<TItem>)getSelectionModel().getSelectedItem();
            if (selected.getValue() == null) return;
            TItem parent = selected.getValue();
            if (parent.getKind() != TreeKind.CATEGORY) return;
            try {
                TItem newItem = dataProvider.addTreeItem(EntityUtils.createCategory((Category)parent));
                if (newItem == null) return;
                selected.getChildren().add(new TreeItem<>(newItem));
            } catch (Exception e) { e.printStackTrace(); }
        });
        addNotMenuItem = new MenuItem("+ Добавить запись");
        addNotMenuItem.setOnAction(event -> {
            TreeItem<TItem> selected = (TreeItem<TItem>)getSelectionModel().getSelectedItem();
            if (selected.getValue() == null) return;
            TItem parent = selected.getValue();
            if (parent.getKind() != TreeKind.CATEGORY) return;
            try {
                TItem newItem = dataProvider.addTreeItem(EntityUtils.createNotice((Category)parent));
                if (newItem == null) return;
                selected.getChildren().add(new TreeItem<>(newItem));
            } catch (Exception e) { e.printStackTrace(); }
        });
        removeMenuItem = new MenuItem("- Удалить");
        removeMenuItem.setOnAction(event -> {
            TreeItem<TItem> selected = (TreeItem<TItem>)getSelectionModel().getSelectedItem();
            if (selected.getValue() == null) return;
            try {
                dataProvider.removeTreeItem(selected);
                selected.getParent().getChildren().remove(selected);
            } catch (Exception e) { e.printStackTrace(); }
        });
        contextMenu.getItems().setAll(addCatMenuItem, addNotMenuItem, removeMenuItem);
        setContextMenu(contextMenu);
    }

    @PostConstruct
    public void init() {
        root = new TreeItem<>(new Category(-1, "Scientia potentia est"));
        root.setExpanded(true);
        root.getChildren().addAll(dataProvider.getTreeCache());
        setRoot(root);
        setShowRoot(false);

        TreeTableColumn<TItem, String> column = new TreeTableColumn<>("Дерево знаний");
        column.setPrefWidth(280);
        column.setResizable(true);
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<TItem, String> p) -> new ReadOnlyStringWrapper(
                String.format("%s - %s", p.getValue().getValue().getId(), p.getValue().getValue().getName()))
        );
        getColumns().add(column);

        getSelectionModel().selectedItemProperty().addListener((observableValue, oldSelection, newSelection) -> {
            if (TreeItem.class == newSelection.getClass()) {
                TItem item = ((TreeItem<TItem>)newSelection).getValue();
                ((TreeItem<TItem>)newSelection).setValue(item);
                if (item != null && TreeKind.NOTICE == item.getKind()) {
                    Notice notice = dataProvider.getNoticeCache().get(item.getId());
                    if (notice != null) itemEvent.fire(new NoticeEvent(notice));
                }
                if (item != null) {
                    selectedEvent.fire(new SelectedEvent(item));
                }
                addCatMenuItem.setVisible(item.getKind() == TreeKind.CATEGORY);
                addNotMenuItem.setVisible(item.getKind() == TreeKind.CATEGORY);
            }
        });

        setRowFactory(new Callback<TreeTableView, TreeTableRow<TreeItem<TItem>>>() {
            @Override
            public TreeTableRow<TreeItem<TItem>> call(final TreeTableView param) {
                final TreeTableRow<TreeItem<TItem>> row = new TreeTableRow<>();

                // Drag and Drop
                row.setOnDragDetected(event -> {
                    TreeItem<TItem> selected = (TreeItem<TItem>) getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        Dragboard db = CategoryTree.this.startDragAndDrop(TransferMode.ANY);
                        db.setDragView(row.snapshot(null, null));

                        ClipboardContent content = new ClipboardContent();
                        content.putString(selected.getValue().getName());
                        db.setContent(content);
                        event.consume();
                    }
                });
                row.setOnDragOver(event -> {
                    Dragboard db = event.getDragboard();
                    if (event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                    event.consume();
                });
                row.setOnDragDropped(event -> {
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (event.getDragboard().hasString()) {
                        if (!row.isEmpty()) {
                            int dropIndex = row.getIndex();
                            TreeItem<TreeItem<TItem>> droppedon = row.getTreeItem();
                            success = true;
                        }
                    }
                    event.setDropCompleted(success);
                    event.consume();
                });

                return row;
            }
        });
    }

    public void setSelectedItem(TItem selected) {
        System.out.println("refreshTree...");
        if (selected == null) return;
        TreeItem<TItem> ti = (TreeItem<TItem>)this.getSelectionModel().getSelectedItem();
        if (ti != null) {
            ti.setValue(selected);
            refresh();
        }
    }
}
