package com.kbmsfx.gui.right;

import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

/**
 * Created by Alex on 15.07.2016.
 */
@Dependent
public class VDragboardPanel extends TitledPane {

    VBox wrap;

    public VDragboardPanel() {
        super();
        //setMinHeight(50);
        setMinWidth(120);
        setPadding(new Insets(10));
        setText("Записи");
        setExpanded(true);
        addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (isCollapsible()) setExpanded(true);
        });
        HBox.setMargin(this, new Insets(10, 0, 10, 0));
    }

    @PostConstruct
    public void init() {

        wrap = new VBox();

        MenuButton m = new MenuButton("Eats");
        m.setPrefWidth(100);
        m.setPopupSide(Side.LEFT);
        m.getItems().addAll(new MenuItem("Burger"), new MenuItem("Hot Dog"));

        MenuButton m2 = new MenuButton("Drinks");
        m2.setPrefWidth(100);
        m2.setPopupSide(Side.LEFT);
        m2.getItems().addAll(new MenuItem("Juice"), new MenuItem("Milk"));

        wrap.getChildren().addAll(m, m2);
        setContent(wrap);
    }
}
