package com.kbmsfx;

/**
 * Created with IntelliJ IDEA.
 * User: Alexey Balyschev
 * Date: 10.07.16
 */
import com.kbmsfx.gui.left.LeftPanel;
import com.kbmsfx.gui.center.MainCenterPanel;
import com.kbmsfx.utils.CacheData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.*;
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
    private LeftPanel leftPanel;

    @Inject
    private MainCenterPanel mainCenterPanel;

    public static void main(String[] args) {
        System.out.println("launch app...");
        Application.launch(App.class, args);
    }

    public void start(@Observes ContainerInitialized startEvent) {
        System.out.println("start app from CDI container...");
        App.getInstance().start(this);
    }

    public Pane buildCenter() {
        return mainCenterPanel;
    }

    public Pane buildLeft() {
        return leftPanel;
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

        menuBar.getMenus().addAll(fileMenu);
        return menuBar;
    }
}

