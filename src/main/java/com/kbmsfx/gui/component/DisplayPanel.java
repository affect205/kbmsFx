package com.kbmsfx.gui.component;

import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

/**
 * Created by Alex on 14.07.2016.
 */
@Dependent
public class DisplayPanel extends FlowPane {

    public DisplayPanel() {
        super();
    }

    @PostConstruct
    public void init() {
        setPadding(new Insets(10, 10, 10, 10));
        setVgap(4);
        setHgap(4);
        setPrefWrapLength(300);
    }
}
