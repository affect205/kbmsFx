package com.kbmsfx.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 13.07.16
 */
public class StringUtils {

    public static String substring(String str, int length) {
        try {
            return str.substring(0, length);
        } catch(Exception e) {
            return str;
        }
    }

}
