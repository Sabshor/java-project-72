package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlsController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
public final class App {
    private static final String DB_NAME_LOCAL = "project";
    public static void main(String[] args) throws SQLException, IOException {
        Javalin app = getApp();
        app.start(getPort());
    }

    public static Javalin getApp() throws SQLException, IOException {
        setConnection();

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get(NamedRoutes.rootPath(), RootController::index);
        app.post(NamedRoutes.urlsPath(), UrlsController::create);
        app.get(NamedRoutes.urlsPath(), UrlsController::index);
        app.get(NamedRoutes.urlPath("{id}"), UrlsController::show);
        return app;
    }

    private static TemplateEngine createTemplateEngine() {
        /*Чтобы при проверке приложения автотестами шаблоны подгружались из нужного места, нам потребуется
        явно указать расположение шаблонов. Для этого нужно будет создать инстанс движка шаблонизатора и используя
        ResourceCodeResolver указать в нем путь к шаблонам:*/
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }

    private static void setConnection() throws SQLException, IOException {
        var hikariConfig = new HikariConfig();
        var urlLocal = String.format("jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1;", DB_NAME_LOCAL);
        var url = System.getenv().getOrDefault("JDBC_DATABASE_URL", urlLocal);
        hikariConfig.setJdbcUrl(url);

        var dataSource = new HikariDataSource(hikariConfig);
        var sql = readResourceFile("schema.sql");

        log.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;
    }

    private static String readResourceFile(String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

}
