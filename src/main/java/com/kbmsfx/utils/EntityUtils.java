package com.kbmsfx.utils;

import com.kbmsfx.dto.CategoryDTO;
import com.kbmsfx.dto.NoticeDTO;
import com.kbmsfx.entity.Category;
import com.kbmsfx.entity.Notice;
import com.kbmsfx.entity.TItem;
import com.kbmsfx.enums.TreeKind;

import java.util.Date;

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
}
