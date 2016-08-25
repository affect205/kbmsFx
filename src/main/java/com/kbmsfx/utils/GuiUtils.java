package com.kbmsfx.utils;

import com.kbmsfx.enums.IconKind;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.foundation.Foundation;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Created by Alex on 07.08.2016.
 */
public class GuiUtils {

    public static Node buildIcon(IconKind kind) {
        Foundation path = null;
        Color color = Color.LIGHTSLATEGRAY;

        switch (kind) {
            case CATEGORY:
                path = Foundation.FOLDER;
                break;
            case NOTICE:
                path = Foundation.CLIPBOARD_PENCIL;
                break;
            case SEARCH:
                path = Foundation.MAGNIFYING_GLASS;
                break;
            case CLOSE:
                path = Foundation.X;
                break;
            case USER:
                path = Foundation.TORSO;
                break;
            case CATEGORY_ADD:
                path = Foundation.FOLDER_ADD;
                break;
            case DELETE:
                path = Foundation.X;
                break;
            case CONTENT_VIEW:
                path = Foundation.EYE;
                break;
            case CONTENT_EDIT:
                path = Foundation.HTML5;
                break;
        }

        if (path == null) return null;
        FontIcon icon = new FontIcon(path);
        icon.setIconSize(18);
        icon.setIconColor(color);
        return icon;
    }

    public static Node buildMBIcon(IconKind kind) {
        FontIcon icon = (FontIcon) buildIcon(kind);
        return icon;
    }
}
