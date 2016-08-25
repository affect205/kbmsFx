package com.kbmsfx.utils;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 12.07.16
 */
public class DBUtils {
    public static void close(AutoCloseable c) throws Exception {
        if (c != null) c.close();
    }

    public static PreparedStatement setIntArray(PreparedStatement stmt, Collection<Integer> values, int pos) throws Exception {
        for (Integer v : values) stmt.setInt(pos++, v);
        return stmt;
    }

    public static String buildParams(int cnt) {
        List<String> params = new ArrayList<>();
        for (int ndx=0; ndx < cnt; ++ndx) {
            params.add("?");
        }
        return String.join(", ", params);
    }
}
