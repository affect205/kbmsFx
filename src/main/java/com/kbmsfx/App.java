package com.kbmsfx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;

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
        System.out.print("init javaFx...");
        App.stage = stage;
        App.instance = this;
        System.out.print("init CDI container...");
        Weld weld = new Weld();
        weld.initialize();
    }

    public void start(AppLoader loader) {
        System.out.print("start javaFx...");
        stage.setTitle("HTML");
        stage.setWidth(1000);
        stage.setHeight(500);
        Scene scene = new Scene(new Group());

        BorderPane root = new BorderPane();
        root.setCenter(loader.buildCenter());
        root.setLeft(loader.buildLeft());
        root.setTop(loader.buildTop());
        scene.setRoot(root);

        stage.setScene(scene);
        stage.show();
    }
}
