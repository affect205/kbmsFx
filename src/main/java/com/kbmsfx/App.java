package com.kbmsfx;

/**
 * Created with IntelliJ IDEA.
 * User: Alexey Balyschev
 * Date: 10.07.16
 */
import com.kbmsfx.gui.component.CategoryTree;
import com.kbmsfx.gui.component.DisplayPanel;
import com.kbmsfx.gui.component.HDragboardPanel;
import com.kbmsfx.gui.component.VDragboardPanel;
import com.kbmsfx.utils.CacheData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class App extends Application {

    @Inject
    private CacheData dataProvider;

    @Inject
    private DisplayPanel displayPanel;

    @Inject
    private CategoryTree categoryTree;

    @Inject
    HDragboardPanel hDragboardPanel;

    @Inject
    VDragboardPanel vDragboardPanel;

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
        stage.setWidth(1000);
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

        BorderPane center = new BorderPane();
        DisplayPanel dp = getBean(DisplayPanel.class);
        dp.getChildren().add(titledPane);
        center.setCenter(dp);
        center.setRight(getBean(VDragboardPanel.class));
        center.setTop(getBean(HDragboardPanel.class));

        return center;
    }

    protected Control buildLeft() {
        return getBean(CategoryTree.class);
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
        return new HashSet<>(Arrays.asList(dataProvider, displayPanel, categoryTree, hDragboardPanel, vDragboardPanel));
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

