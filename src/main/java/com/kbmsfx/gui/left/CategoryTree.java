package com.kbmsfx.gui.left;

import com.kbmsfx.entity.Category;
import com.kbmsfx.entity.Notice;
import com.kbmsfx.entity.TItem;
import com.kbmsfx.enums.IconKind;
import com.kbmsfx.enums.TreeKind;
import com.kbmsfx.events.RefreshAllCategoryQAEvent;
import com.kbmsfx.events.TItemEvent;
import com.kbmsfx.utils.CacheData;
import com.kbmsfx.utils.EntityUtils;
import com.kbmsfx.utils.GuiUtils;
import com.kbmsfx.utils.StringUtils;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.input.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Created by Alex on 15.07.2016.
 */
@Dependent
public class CategoryTree extends TreeTableView {

    @Inject
    private CacheData dataProvider;

    @Inject
    private Event<TItemEvent> itemEvent;

    @Inject
    private Event<RefreshAllCategoryQAEvent> refreshAllCategoryQAEvent;

    private TreeItem<TItem> root;

    private MenuItem addCatMenuItem;
    private MenuItem addNotMenuItem;
    private MenuItem removeMenuItem;
    private ContextMenu contextMenu;
    private boolean isFiltered = false;

    public CategoryTree() {
        super();
        contextMenu = new ContextMenu();
        addCatMenuItem = new MenuItem("Добавить категорию", GuiUtils.buildMBIcon(IconKind.CATEGORY_ADD));
        addCatMenuItem.setOnAction(event -> {
            TreeItem<TItem> selected = (TreeItem<TItem>)getSelectionModel().getSelectedItem();
            if (selected.getValue() == null) return;
            TItem parent = selected.getValue();
            if (parent.getKind() != TreeKind.CATEGORY) return;
            try {
                TItem newItem = dataProvider.addTreeItem(EntityUtils.createCategory((Category)parent));
                if (newItem == null) return;
                selected.getChildren().add(EntityUtils.buildTreeItem(newItem));
            } catch (Exception e) { e.printStackTrace(); }
        });
        addNotMenuItem = new MenuItem("Добавить запись", GuiUtils.buildMBIcon(IconKind.NOTICE));
        addNotMenuItem.setOnAction(event -> {
            TreeItem<TItem> selected = (TreeItem<TItem>)getSelectionModel().getSelectedItem();
            if (selected.getValue() == null) return;
            TItem parent = selected.getValue();
            if (parent.getKind() != TreeKind.CATEGORY) return;
            try {
                TItem newItem = dataProvider.addTreeItem(EntityUtils.createNotice((Category)parent));
                if (newItem == null) return;
                selected.getChildren().add(EntityUtils.buildTreeItem(newItem));
            } catch (Exception e) { e.printStackTrace(); }
        });
        removeMenuItem = new MenuItem("Удалить", GuiUtils.buildMBIcon(IconKind.DELETE));
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
        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            refresh();
        });
    }

    @PostConstruct
    public void init() {
        root = EntityUtils.buildTreeItem(new Category(-1, "Scientia potentia est".toUpperCase()));
        root.setExpanded(true);
        root.getChildren().addAll(dataProvider.getTreeCache());
        setRoot(root);
        setShowRoot(true);

        TreeTableColumn<TItem, String> column = new TreeTableColumn<>("Дерево знаний");
        column.setPrefWidth(280);
        column.setResizable(true);
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<TItem, String> p) -> {
            String name = "";
            try {
                name = p.getValue().getValue().getName();
            } catch (Exception e) {
                try {
                    name = p.getValue().getChildren().get(0).getValue().getName();
                } catch (Exception e2) {
                    name = "error!";
                }
            }
            return new ReadOnlyStringWrapper(name);
        });
        getColumns().add(column);

        getSelectionModel().selectedItemProperty().addListener((observableValue, oldSelection, newSelection) -> {
            if (newSelection != null && TreeItem.class == newSelection.getClass()) {
                TreeItem<TItem> ti = ((TreeItem<TItem>)newSelection);
                TItem item = ti.getValue();
                if (item != null) {
                    itemEvent.fire(new TItemEvent(ti));
                }
                // @TODO think about CRUD for filtered data
                addCatMenuItem.setVisible(item.getKind() == TreeKind.CATEGORY && !isFiltered);
                addNotMenuItem.setVisible(item.getKind() == TreeKind.CATEGORY && !isFiltered);
                removeMenuItem.setVisible(!isFiltered);
            }
        });

        setRowFactory(param -> {
            final TreeTableRow<TreeItem<TItem>> row = new TreeTableRow<>();

            row.setOnDragDetected(event -> {
                TreeItem<TItem> selected = (TreeItem<TItem>) getSelectionModel().getSelectedItem();
                // @TODO think about DnD for filtered data
                if (selected != null && !isFiltered) {
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
                boolean openDialog = event.getDragboard().hasString() && !row.isEmpty() ? true : false;
                event.setDropCompleted(true);
                event.consume();
                if (openDialog) {
                    Platform.runLater(() -> openDialog(row.getTreeItem(), event));
                }
            });

            return row;
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

    public void filterBy(String filter) {
        if (StringUtils.isEmpty(filter)) {
            setRoot(root);
            isFiltered = false;
        }
        else {
            TreeItem<TItem> filteredRoot = new TreeItem<>();
            filteredRoot.setExpanded(true);
            filter(root, filter, filteredRoot);
            setRoot(filteredRoot);
            isFiltered = true;
        }
        refresh();
    }

    protected void filter(TreeItem<TItem> root, String filter, TreeItem<TItem> filteredRoot) {
        for (TreeItem<TItem> child : root.getChildren()) {
            TreeItem<TItem> filteredChild = EntityUtils.buildTreeItem(child.getValue());
            filteredChild.setExpanded(true);
            filter(child, filter, filteredChild );
            if (!filteredChild.getChildren().isEmpty() || isMatch(filteredChild.getValue(), filter)) {
                filteredRoot.getChildren().add(filteredChild);
            }
        }
    }

    protected boolean isMatch(TItem value, String filter) {
        if (value == null) return false;
        boolean nameMatch = value.getName().toLowerCase().contains(filter.toLowerCase().trim());
        if (value.getKind() == TreeKind.NOTICE) {
            String content = ((Notice)value).getContent();
            return nameMatch || content.toLowerCase().contains(filter.toLowerCase().trim());
        }
        return nameMatch;
    }

    protected void openDialog(TreeItem droppedOn, DragEvent event) {
        if (droppedOn != null && droppedOn.getValue() != null && droppedOn.getValue() instanceof TItem) {
            TItem destItem = (TItem)droppedOn.getValue();
            System.out.printf("Dest item: %s\n", destItem.toString());

            if (event.getGestureSource().getClass() == CategoryTree.class) {
                CategoryTree tree = (CategoryTree) event.getGestureSource();
                TreeItem<TItem> ti =  (TreeItem<TItem>)tree.getSelectionModel().getSelectedItem();
                if (ti != null && ti.getValue() != null) {
                    TItem srcItem = ti.getValue();
                    System.out.printf("Source item: %s\n", srcItem.toString());

                    if (destItem.getKind() == TreeKind.CATEGORY && !destItem.customEquals(EntityUtils.getParent(srcItem))) {
                        String kindDesc = srcItem.getKind() == TreeKind.CATEGORY ? "категорию": "запись";
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Смена категории");
                        confirm.setContentText(String.format("Переместить %s \"%s\" в категорию \"%s\"?",
                                kindDesc, srcItem.getName(), destItem.getName()));
                        confirm.setHeaderText(null);
                        Optional<ButtonType> result = confirm.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            if (dataProvider.changeItemCategory(ti, droppedOn)) {
                                refreshAllCategoryQAEvent.fire(new RefreshAllCategoryQAEvent(ti));
                                refresh();
                            }
                        }
                    }
                }
            }
        }
    }
}
