package com.kbmsfx;

/**
 * Created with IntelliJ IDEA.
 * User: Alexey Balyschev
 * Date: 10.07.16
 */
import com.kbmsfx.entity.Category;
import com.kbmsfx.entity.TItem;
import com.kbmsfx.gui.component.DisplayPanel;
import com.kbmsfx.utils.CacheData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class App extends Application {

    @Inject
    private CacheData dataProvider;

    @Inject
    private DisplayPanel displayPanel;

    private static  WeldContainer container;

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
        container = weld.initialize();
        System.out.print("launch javaFx...");
        Application.launch(App.class, args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("HTML");
        stage.setWidth(500);
        stage.setHeight(500);
        Scene scene = new Scene(new Group());

        BorderPane root = new BorderPane();
        root.setCenter(buildCenter());
        root.setLeft(buildLeft());
        root.setTop(buildTop());
        scene.setRoot(root);

        stage.setScene(scene);
        stage.show();
    }

    protected Pane buildCenter() {

        final HTMLEditor htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(600);
        htmlEditor.setHtmlText(INITIAL_TEXT);
        TitledPane titledPane = new TitledPane("HTML editor", htmlEditor);


        TitledPane t1 = new TitledPane("T1", new Button("B1"));
        TitledPane t2 = new TitledPane("T2", new Button("B2"));
        TitledPane t3 = new TitledPane("T3", new Button("B3"));
        Accordion accordion = new Accordion();
        accordion.getPanes().addAll(t1, t2, t3);
        //accordion.setRotate(270);

        MenuButton m = new MenuButton("Eats");
        m.setPrefWidth(100);
        m.setPopupSide(Side.LEFT);
        m.getItems().addAll(new MenuItem("Burger"), new MenuItem("Hot Dog"));

        MenuButton m2 = new MenuButton("Drinks");
        m2.setPrefWidth(100);
        m2.setPopupSide(Side.LEFT);
        m2.getItems().addAll(new MenuItem("Juice"), new MenuItem("Milk"));

        MenuButton m3 = new MenuButton("Eats");
        m3.setPrefWidth(100);
        m3.setPopupSide(Side.BOTTOM);
        m3.getItems().addAll(new MenuItem("Burger"), new MenuItem("Hot Dog"));

        MenuButton m4 = new MenuButton("Drinks");
        m4.setPrefWidth(100);
        m4.setPopupSide(Side.BOTTOM);
        m4.getItems().addAll(new MenuItem("Juice"), new MenuItem("Milk"));

        VBox right = new VBox();
        right.getChildren().addAll(m, m2);

        HBox top = new HBox();
        top.getChildren().addAll(m3, m4);


        BorderPane center = new BorderPane();
        DisplayPanel dp = getBean(DisplayPanel.class);
        dp.getChildren().add(titledPane);
        center.setCenter(dp);
        center.setRight(right);
        center.setTop(top);

        return center;
    }

    protected Control buildLeft() {

        CacheData dp = getBean(CacheData.class);
        List<TreeItem<TItem>> treeData = dp.getTreeCache();

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

        TreeTableColumn<TItem, String> column = new TreeTableColumn<>("Categories");
        column.setPrefWidth(200);
        column.setResizable(true);

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

    public Set<Object> getAllBeans() {
        return new HashSet<>(Arrays.asList(dataProvider, displayPanel));
    }

    public <T> T getBean(Class<T> type) {
        App context = container.instance().select(App.class).get();
        if (type == App.class)  {
            return (T)context;
        }

        for (Object obj : context.getAllBeans()) {
            if (obj.getClass() == type) {
                return (T)obj;
            }
        }
        return null;
    }
}

