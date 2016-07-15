package com.kbmsfx.gui.component;

import com.kbmsfx.entity.Category;
import com.kbmsfx.entity.TItem;
import com.kbmsfx.utils.CacheData;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.*;
import javafx.util.Callback;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Created by Alex on 15.07.2016.
 */
@Dependent
public class CategoryTree extends TreeTableView {

    @Inject
    private CacheData dataProvider;

    public CategoryTree() {
        super();
    }

    @PostConstruct
    public void init() {
        TreeItem<TItem> root = new TreeItem<>(new Category(-1, "Scientia potentia est"));
        root.setExpanded(true);
        root.getChildren().addAll(dataProvider.getTreeCache());

        TreeTableColumn<TItem, String> column = new TreeTableColumn<>("Categories");
        column.setPrefWidth(280);
        column.setResizable(true);
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<TItem, String> p) -> new ReadOnlyStringWrapper(
                p.getValue().getValue().getName())
        );

        setRoot(root);
        getColumns().add(column);


        setRowFactory(new Callback<TreeTableView, TreeTableRow<TreeItem<TItem>>>() {
            @Override
            public TreeTableRow<TreeItem<TItem>> call(final TreeTableView param) {
                final TreeTableRow<TreeItem<TItem>> row = new TreeTableRow<>();

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
}
