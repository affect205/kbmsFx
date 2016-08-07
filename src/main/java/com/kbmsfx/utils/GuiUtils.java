package com.kbmsfx.utils;

import com.kbmsfx.enums.IconKind;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Alex on 07.08.2016.
 */
public class GuiUtils {

    public static Node buildIcon(IconKind kind) {
        String path = null;

        switch (kind) {
            case CATEGORY:
                path = "icons/folder_24.png";
                break;
            case NOTICE:
                path = "icons/notice_24.png";
                break;
            case SEARCH:
                path = "icons/search_24.png";
                break;
        }
        ImageView icon = path == null ? null : new ImageView(new Image(GuiUtils.class.getClassLoader().getResourceAsStream(path)));
        icon.setScaleX(0.7);
        icon.setScaleY(0.7);
        return icon;
    }

    public static Node buildIcon(IconKind kind, double scale) {
        ImageView icon = (ImageView) buildIcon(kind);
        icon.setScaleX(scale);
        icon.setScaleY(scale);
        return icon;
    }
}
