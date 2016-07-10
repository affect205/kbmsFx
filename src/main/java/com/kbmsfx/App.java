package com.kbmsfx;

/**
 * Created with IntelliJ IDEA.
 * User: Alexey Balyschev
 * Date: 10.07.16
 */
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

public class App extends Application {

    private final String INITIAL_TEXT = "Lorem ipsum dolor sit "
            + "amet, consectetur adipiscing elit. Nam tortor felis, pulvinar "
            + "in scelerisque cursus, pulvinar at ante. Nulla consequat"
            + "congue lectus in sodales. Nullam eu est a felis ornare "
            + "bibendum et nec tellus. Vivamus non metus tempus augue auctor "
            + "ornare. Duis pulvinar justo ac purus adipiscing pulvinar. "
            + "Integer congue faucibus dapibus. Integer id nisl ut elit "
            + "aliquam sagittis gravida eu dolor. Etiam sit amet ipsum "
            + "sem.";

    @Override
    public void start(Stage stage) {
        stage.setTitle("HTML");
        stage.setWidth(500);
        stage.setHeight(500);
        Scene scene = new Scene(new Group());

        BorderPane root = new BorderPane();
        root.setCenter(buildCenter());
        root.setLeft(buildLeft());
        scene.setRoot(root);

        stage.setScene(scene);
        stage.show();
    }

    protected Control buildCenter() {
        final HTMLEditor htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(600);
        htmlEditor.setHtmlText(INITIAL_TEXT);
        return htmlEditor;
    }

    protected Control buildLeft() {

        TreeItem<String> childNode1 = new TreeItem<>("Node 1");
        TreeItem<String> childNode2 = new TreeItem<>("Node 2");
        TreeItem<String> childNode3 = new TreeItem<>("Node 3");

        TreeItem<String> root = new TreeItem<>("Root");
        root.setExpanded(true);

        root.getChildren().setAll(childNode1, childNode2, childNode3);

        TreeTableColumn<String, String> column = new TreeTableColumn<>("Column");
        column.setPrefWidth(150);

        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<String, String> p) -> new ReadOnlyStringWrapper(
                p.getValue().getValue()));

        TreeTableView<String> treeTableView = new TreeTableView<>(root);
        treeTableView.getColumns().add(column);
        return treeTableView;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

