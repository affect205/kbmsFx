package com.kbmsfx.gui.component;

import javafx.geometry.Side;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

/**
 * Created by Alex on 15.07.2016.
 */
@Dependent
public class VDragboardPanel extends VBox {

    public VDragboardPanel() {
        super();
    }

    @PostConstruct
    public void init() {

        MenuButton m = new MenuButton("Eats");
        m.setPrefWidth(100);
        m.setPopupSide(Side.LEFT);
        m.getItems().addAll(new MenuItem("Burger"), new MenuItem("Hot Dog"));

        MenuButton m2 = new MenuButton("Drinks");
        m2.setPrefWidth(100);
        m2.setPopupSide(Side.LEFT);
        m2.getItems().addAll(new MenuItem("Juice"), new MenuItem("Milk"));

        getChildren().addAll(m, m2);

    }
}
