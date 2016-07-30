package com.kbmsfx.gui.component;

import com.kbmsfx.entity.Category;
import com.kbmsfx.entity.Notice;
import com.kbmsfx.entity.TItem;
import com.kbmsfx.enums.TreeKind;
import com.kbmsfx.events.NoticeEvent;
import com.kbmsfx.events.RefreshTreeEvent;
import com.kbmsfx.events.SelectedEvent;
import com.kbmsfx.utils.CacheData;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.util.Callback;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

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

    private MenuItem addMenuItem;
    private MenuItem removeMenuItem;
    private ContextMenu contextMenu;

    public CategoryTree() {
        super();
        contextMenu = new ContextMenu();
        addMenuItem = new MenuItem("+ Добавить");
        removeMenuItem = new MenuItem("- Удалить");
        addMenuItem.setOnAction(event -> {
            System.out.println("add item...");
            TreeItem<TItem> selected = (TreeItem<TItem>)getSelectionModel().getSelectedItem();
            System.out.println(selected.getValue());
        });
        removeMenuItem.setOnAction(event -> {
            System.out.println("remove item...");
            TreeItem<TItem> selected = (TreeItem<TItem>)getSelectionModel().getSelectedItem();
            System.out.println(selected.getValue());
        });
        contextMenu.getItems().setAll(addMenuItem, removeMenuItem);
        setContextMenu(contextMenu);
    }

    @PostConstruct
    public void init() {
        root = new TreeItem<>(new Category(-1, "Scientia potentia est"));
        root.setExpanded(true);
        root.getChildren().addAll(dataProvider.getTreeCache());
        setRoot(root);

        TreeTableColumn<TItem, String> column = new TreeTableColumn<>("Categories");
        column.setPrefWidth(280);
        column.setResizable(true);
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<TItem, String> p) -> new ReadOnlyStringWrapper(
                p.getValue().getValue().getName())
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
                addMenuItem.setVisible(item.getKind() == TreeKind.CATEGORY);
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
