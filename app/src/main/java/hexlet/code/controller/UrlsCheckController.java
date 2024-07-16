package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.Unirest;

import java.sql.SQLException;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsCheckController {
    public static void create(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Site not found"));

        /*(try UnirestInstance unirest = Unirest.primaryInstance())  {*/
        try {
            //unirest.config().connectTimeout(5000);
            /*String body = unirest.get(url.getName())
                    .asString()
                    .getBody();
            System.out.println("response=" + body);

            Integer status = unirest.get(url.getName())
                    .asString()
                    .getStatus();
            System.out.println("status=" + status);

            Unirest.get(url.getName())
                    .asJsonAsync(response -> {
                        int code = response.getStatus();
                        JsonNode body3 = response.getBody();
                        System.out.println("code=" + code + ";333response=" + body3);
                    });


            String result = (String) Unirest.get(url.getName())
                    .asJson()
                    .getHeaders()
                    .toString();
            System.out.println("response2=" + result);

            Unirest.get("http://somewhere")
                    .asJson()
                    .ifSuccess(response -> {//someSuccessMethod(response)
                        System.out.println("ok=" + response); })
                    .ifFailure(response -> {
                        System.err.println("Oh No! Status" + response.getStatus());
                        response.getParsingError().ifPresent(e -> {
                            System.err.println("Parsing Exception: " + e);
                            System.err.println("Original body: " + e.getOriginalBody());
                        });
                    });*/
            var response = Unirest.get(url.getName())
                    .asString();
            /*var response = unirest.get(url.getName())
                    .asString();*/
            int code = response.getStatus();
            //var body = response.getBody();
            var urlCheck = UrlCheck.builder()
                    .urlId(id)
                    .statusCode(code)
                    .build();
            UrlCheckRepository.save(urlCheck);
            var urlChecks = UrlCheckRepository.getEntityDetails(id);
            var page = new UrlPage(url, urlChecks);
            ctx.render("urls/show.jte", model("page", page));
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный адрес");
            ctx.sessionAttribute("flashType", "warning");
            ctx.redirect(NamedRoutes.urlPath(id));
        }
    }
}
