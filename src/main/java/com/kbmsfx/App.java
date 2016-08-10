package com.kbmsfx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;

import java.net.URL;

/**
 * Created by Alex on 16.07.2016.
 */
public class App extends Application {

    private static App instance;
    private static Stage stage;

    public App() {}

    public static App getInstance() {
        return instance;
    }

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage stage) {
        System.out.println("init app...");
        App.stage = stage;
        App.instance = this;
        System.out.print("init CDI container...");
        Weld weld = new Weld();
        weld.initialize();
    }

    public void start(AppLoader loader) {
        System.out.println("start app...");
        stage.setTitle("HTML");
        stage.setWidth(1200);
        stage.setHeight(680);
        Scene scene = new Scene(new Group());

        URL url = this.getClass().getClassLoader().getResource("custom.css");
        if (url == null) {
            System.out.println("Resource not found. Aborting."); System.exit(-1);
        }
        String css = url.toExternalForm();
        scene.getStylesheets().add(css);

        BorderPane root = new BorderPane();
        root.setCenter(loader.buildCenter());
        root.setLeft(loader.buildLeft());
        root.setTop(loader.buildTop());
        scene.setRoot(root);

        stage.setScene(scene);
        stage.show();
    }
}
