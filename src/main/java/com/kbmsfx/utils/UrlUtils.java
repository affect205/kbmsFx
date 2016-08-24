package com.kbmsfx.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * Created by abalyshev on 24.08.16.
 */
public class UrlUtils {

    public static String loadHtml(String url) {
        Charset charset = Charset.forName("UTF8");
        StringBuffer responseBuffer = new StringBuffer();
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setRequestProperty("Accept-Encoding", "identity");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            InputStreamReader stream = new InputStreamReader(connection.getInputStream(), charset);
            BufferedReader reader = new BufferedReader(stream);
            String read = "";
            while ((read = reader.readLine()) != null) {
                responseBuffer.append(read);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return responseBuffer.toString();
    }
}
