package hexlet.code.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Utils {

    public static boolean checkUrl(String url) {
        try {
            new URI(url).toURL();
        } catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public static String getVerifyUrl(String url) {
        try {
            var uri = new URI(url).toURL();
            return uri.getProtocol().concat("://").concat(uri.getAuthority());
        } catch (MalformedURLException | URISyntaxException e) {
            return "";
        }
    }
    public static String getFormattedData(Timestamp date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
    }
}
