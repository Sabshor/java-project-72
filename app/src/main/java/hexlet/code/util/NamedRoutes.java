package hexlet.code.util;

public class NamedRoutes {
    public static String rootPath() {
        return "/";
    }
    public static String urlsPath() {
        return "/urls";
    }

    public static String urlPath(Long id) {
        return urlPath(String.valueOf(id));
    }

    public static String urlPath(String id) {
        return urlsPath().concat("/").concat(id);
    }

    public static String urlCheckPath(Long id) {
        return urlCheckPath(String.valueOf(id));
    }

    public static String urlCheckPath(String id) {
        return urlsPath().concat("/").concat(id).concat("/checks");
    }
}
