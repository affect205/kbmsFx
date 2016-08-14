package com.kbmsfx.utils;

import com.kbmsfx.dto.CategoryDTO;
import com.kbmsfx.dto.NoticeDTO;
import com.kbmsfx.entity.Category;
import com.kbmsfx.entity.Notice;
import com.kbmsfx.entity.TItem;
import com.kbmsfx.enums.IconKind;
import com.kbmsfx.enums.TreeKind;
import javafx.scene.control.TreeItem;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alex Balyschev on 14.07.2016.
 */
public class EntityUtils {

    public static NoticeDTO toNoticeDTO(TItem item) {
        if (item == null || item.getKind() != TreeKind.NOTICE) return null;
        Notice notice = (Notice)item;
        return new NoticeDTO(notice.getId(), notice.getName(), notice.getContent(), notice.getCategory().getId(), notice.getSorting());
    }

    public static CategoryDTO toCategoryDTO(TItem item) {
        if (item == null || item.getKind() != TreeKind.CATEGORY) return null;
        Category category = (Category)item;
        return new CategoryDTO(category.getId(), category.getName(), category.getParent() == null ? -1 : category.getParent().getId(), category.getSorting());
    }

    public static Category createCategory(Category parent) {
        return new Category(-1, String.format("Категория %s", new Date().getTime()), parent, 1);
    }

    public static Notice createNotice(Category parent) {
        return new Notice(-1, String.format("Запись %s", new Date().getTime()), "", parent, 1);
    }

    public static Set<Integer> extractCatIds(TreeItem<TItem> ti) {
        Set<Integer> result = new HashSet<>();
        if (ti == null || ti.getValue() == null) return result;
        TItem item = ti.getValue();
        if (item.getKind() != TreeKind.CATEGORY) {
            return result;
        } else {
            result.add(item.getId());
            ti.getChildren().forEach(child-> {
                result.addAll(extractCatIds(child));
            });
        }
        return result;
    }

    public static TreeItem<TItem> buildTreeItem(TItem item) {
        if (item == null) return null;
        IconKind iconKind = item.getKind() == TreeKind.CATEGORY ? IconKind.CATEGORY : IconKind.NOTICE;
        TreeItem<TItem> ti = new TreeItem<>(item, GuiUtils.buildIcon(iconKind));
        if (item.getKind() == TreeKind.CATEGORY) ti.setExpanded(true);
        return ti;
    }

    public static TItem getParent(TItem item) {
        if (item == null) return null;
        if (item.getKind() == TreeKind.CATEGORY) {
            return ((Category)item).getParent();
        } else if (item.getKind() == TreeKind.NOTICE) {
            return((Notice)item).getCategory();
        }
        return null;
    }
}
