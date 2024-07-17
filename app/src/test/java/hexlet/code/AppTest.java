package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.Utils;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    private Javalin app;
    private static MockWebServer mockServer = new MockWebServer();

    @BeforeAll
    public static void beforeAll() throws IOException {
        var body = """
                    <!DOCTYPE html>
                    <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="description" content="Analyzer content">
                            <title>Анализатор страниц</title>
                        </head>
                       <body>
                            <main>
                                <section>
                                    <h1>test mock</h1>
                                </section>
                            </main>
                       </body>
                   </html>
                """;
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(body);
        mockServer.enqueue(mockResponse);
        mockServer.start();
    }

    @AfterAll
    public static void afterAll() throws IOException {
        mockServer.shutdown();
    }

    @BeforeEach
    public final void setUp() throws SQLException, IOException {
        app = App.getApp();
    }

    @Test
    public void testRootPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlPage() throws SQLException {
        var url = new Url(Utils.getVerifyUrl("http://localhost:7070/"));
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);

            var urlTest = UrlRepository.findByName("http://localhost:7070")
                    .orElse(new Url("")).getName();
            assertThat(urlTest).contains("http://localhost:7070");
        });
    }

    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=http://localhost:7070/";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("http://localhost:7070");
        });
    }

    @Test
    public void testCreateErrorUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=badNameSite";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);

            var urls = UrlRepository.findByName("badNameSite");
            assertThat(urls).isEmpty();
        });
    }

    @Test
    public void testUrlNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(99999L));
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testMockRunCheckUrl() {
        HttpUrl baseUrl = mockServer.url("/");
        JavalinTest.test(app, (server, client) -> {
            client.post("/urls", "url=".concat(baseUrl.toString()));
            client.post(NamedRoutes.urlCheckPath(1L));

            var url = UrlRepository.findByName(Utils.getVerifyUrl(baseUrl.toString()));
            assertThat(url).isNotEmpty();

            var urlCheck = UrlCheckRepository.getUrlLastCheck(1L);
            assertThat(urlCheck.getStatusCode()).isEqualTo(200);
            assertThat(urlCheck.getH1()).isEqualTo("test mock");
            assertThat(urlCheck.getTitle()).isEqualTo("Анализатор страниц");
            assertThat(urlCheck.getDescription()).isEqualTo("Analyzer content");

            RecordedRequest request1 = mockServer.takeRequest();
            assertEquals("/", request1.getPath());
        });
    }
}
