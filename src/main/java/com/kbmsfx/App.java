package com.kbmsfx;

/**
 * Created with IntelliJ IDEA.
 * User: Alexey Balyschev
 * Date: 10.07.16
 */
import com.kbmsfx.entity.Category;
import com.kbmsfx.entity.TItem;
import com.kbmsfx.utils.CacheData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class App extends Application {

    private static Stage currentStage;

    @Inject
    private CacheData dataProvider;

    private static CacheData dataProvider2;

    private final String INITIAL_TEXT = "Lorem ipsum dolor sit "
            + "amet, consectetur adipiscing elit. Nam tortor felis, pulvinar "
            + "in scelerisque cursus, pulvinar at ante. Nulla consequat"
            + "congue lectus in sodales. Nullam eu est a felis ornare "
            + "bibendum et nec tellus. Vivamus non metus tempus augue auctor "
            + "ornare. Duis pulvinar justo ac purus adipiscing pulvinar. "
            + "Integer congue faucibus dapibus. Integer id nisl ut elit "
            + "aliquam sagittis gravida eu dolor. Etiam sit amet ipsum "
            + "sem.";

    public static void main(String[] args) {
        System.out.print("init CDI container...");
        Weld weld = new Weld();
        WeldContainer container = weld.initialize();
        container.instance().select(App.class).get();
        Application.launch(App.class, args);
    }

    @PostConstruct
    public void init() {
        if (dataProvider != null) dataProvider2 = dataProvider;
    }

    @Override
    public void start(Stage stage) {
        currentStage = stage;
        currentStage.setTitle("HTML");
        currentStage.setWidth(500);
        currentStage.setHeight(500);
        Scene scene = new Scene(new Group());

        BorderPane root = new BorderPane();
        root.setCenter(buildCenter());
        root.setLeft(buildLeft());
        root.setTop(buildTop());
        scene.setRoot(root);

        currentStage.setScene(scene);
        currentStage.show();
    }

    protected Control buildCenter() {
        final HTMLEditor htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(600);
        htmlEditor.setHtmlText(INITIAL_TEXT);
        return htmlEditor;
    }

    protected Control buildLeft() {

        List<TreeItem<TItem>> treeData = dataProvider2.getTreeCache();

//        TreeItem<String> childNode1 = new TreeItem<>("Node 1");
//        TreeItem<String> childNode2 = new TreeItem<>("Node 2");
//        TreeItem<String> childNode3 = new TreeItem<>("Node 3");
//
//        TreeItem<String> root = new TreeItem<>("Root");
//        root.setExpanded(true);
//
//        root.getChildren().setAll(childNode1, childNode2, childNode3);

        TreeItem<TItem> root = new TreeItem<>(new Category(-1, "Scientia potentia est"));
        root.setExpanded(true);

        root.getChildren().addAll(treeData);

        TreeTableColumn<TItem, String> column = new TreeTableColumn<>("Column");
        column.setPrefWidth(150);

        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<TItem, String> p) -> new ReadOnlyStringWrapper(
                p.getValue().getValue().getName()));

        TreeTableView<TItem> treeTableView = new TreeTableView<>(root);
        treeTableView.getColumns().add(column);
        return treeTableView;
    }

    protected Control buildTop() {

        MenuBar menuBar = new MenuBar();
        //menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        // File menu - new, save, exit
        Menu fileMenu = new Menu("File");
        MenuItem newMenuItem = new MenuItem("New");
        MenuItem saveMenuItem = new MenuItem("Save");
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(actionEvent -> Platform.exit());

        fileMenu.getItems().addAll(newMenuItem, saveMenuItem,
                new SeparatorMenuItem(), exitMenuItem);

        Menu webMenu = new Menu("Web");
        CheckMenuItem htmlMenuItem = new CheckMenuItem("HTML");
        htmlMenuItem.setSelected(true);
        webMenu.getItems().add(htmlMenuItem);

        CheckMenuItem cssMenuItem = new CheckMenuItem("CSS");
        cssMenuItem.setSelected(true);
        webMenu.getItems().add(cssMenuItem);

        Menu sqlMenu = new Menu("SQL");
        ToggleGroup tGroup = new ToggleGroup();
        RadioMenuItem mysqlItem = new RadioMenuItem("MySQL");
        mysqlItem.setToggleGroup(tGroup);

        RadioMenuItem oracleItem = new RadioMenuItem("Oracle");
        oracleItem.setToggleGroup(tGroup);
        oracleItem.setSelected(true);

        sqlMenu.getItems().addAll(mysqlItem, oracleItem,
                new SeparatorMenuItem());

        Menu tutorialManeu = new Menu("Tutorial");
        tutorialManeu.getItems().addAll(
                new CheckMenuItem("Java"),
                new CheckMenuItem("JavaFX"),
                new CheckMenuItem("Swing"));

        sqlMenu.getItems().add(tutorialManeu);

        menuBar.getMenus().addAll(fileMenu, webMenu, sqlMenu);

        return menuBar;
    }
}

