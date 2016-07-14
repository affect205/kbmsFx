package com.kbmsfx.utils;

import com.kbmsfx.entity.TItem;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 12.07.16
 */
public class DBUtils {
    public static void close(AutoCloseable c) throws Exception {
        if (c != null) c.close();
    }

    public static String getString(String str) {
        if (str == null) return "";
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-16");
        } catch (Exception e) { return ""; }
    }

    public static <T extends TItem> T getItemById(List<T> items, int id) {
        if (items == null) return null;
        return items.stream().filter(i -> i.getId() == id).findFirst().orElse(null);
    }
}
