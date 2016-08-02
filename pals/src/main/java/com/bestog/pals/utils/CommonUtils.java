package com.bestog.pals.utils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Class: CommonUtils
 * Important functions for the library
 *
 * @author bestog
 */
public final class CommonUtils {

    /**
     * Make an request via HTTP and deliver back the Response as String
     *
     * @param url         Api-Url, optional with Api-Token
     * @param body        HTTP-Body
     * @param httpMethod  HTTP-Method (GET, POST, ...)
     * @param contentType Content-Type
     * @return String
     */
    public static HashMap<String, String> httpRequest(String url, String body, String httpMethod, String contentType) {
        String response;
        String error;
        HashMap<String, String> out = new HashMap<>();
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(httpMethod);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(body.getBytes().length);
            conn.setRequestProperty("Content-Type", contentType);
            conn.connect();
            OutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(body.getBytes());
            os.flush();
            response = CommonUtils.streamToString(conn.getInputStream());
            out.put("response", response);
            error = CommonUtils.streamToString(conn.getErrorStream());
            out.put("error", error);
        } catch (IOException e) {
            // @todo better logging
            e.printStackTrace();
        }
        return out;
    }

    /**
     * Round double value with low error
     *
     * @param value  Value
     * @param places decimal
     * @return double
     */
    public static double round(double value, int places) {
        // check if places below 0
        places = Math.max(0, places);
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Convert InputStream to String
     *
     * @param stream inputStream
     * @return string
     */
    private static String streamToString(InputStream stream) {
        StringWriter writer = null;
        if (stream != null) {
            try {
                InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
                writer = new StringWriter();
                int n;
                char[] buffer = new char[1024 * 4];
                while (-1 != (n = reader.read(buffer))) {
                    writer.write(buffer, 0, n);
                }
            } catch (IOException e) {
                // @todo better logging
                e.printStackTrace();
            }
        }
        return writer != null ? writer.toString() : "";
    }
}
