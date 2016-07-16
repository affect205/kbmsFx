package com.kbmsfx;

/**
 * Created with IntelliJ IDEA.
 * User: Alexey Balyschev
 * Date: 10.07.16
 */
import com.kbmsfx.gui.component.*;
import com.kbmsfx.utils.CacheData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.jboss.weld.environment.se.events.ContainerInitialized;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class AppLoader {

    @Inject
    private CacheData dataProvider;

    @Inject
    private CategoryTree categoryTree;

    @Inject
    private HDragboardPanel hDragboardPanel;

    @Inject
    private VDragboardPanel vDragboardPanel;

    @Inject
    private DisplayContainer displayContainer;

    public static void main(String[] args) {
        System.out.print("launch javaFx...");
        Application.launch(App.class, args);
    }

    public void start(@Observes ContainerInitialized startEvent) {
        System.out.print("start javaFx from container...");
        App.getInstance().start(this);
    }

    public Pane buildCenter() {
        BorderPane center = new BorderPane();
        displayContainer.setParent(center);
        center.setCenter(displayContainer);
        center.setRight(vDragboardPanel);
        center.setTop(hDragboardPanel);
        return center;
    }

    public Control buildLeft() {
        return categoryTree;
    }

    public Control buildTop() {
        MenuBar menuBar = new MenuBar();

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

