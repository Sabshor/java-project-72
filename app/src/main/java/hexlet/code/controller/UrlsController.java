package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.Utils;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationException;

import java.sql.SQLException;

import static hexlet.code.util.Utils.getVerifyUrl;
import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {
    public static void create(Context ctx) throws SQLException {
        try {
            var urlName = ctx.formParamAsClass("url", String.class)
                    .check(value -> !value.isEmpty(), "Название не должно быть пустым")
                    .check(value -> !value.equals("http://111.ru"), "проверка контроля ошибки!!")
                    .check(Utils::checkUrl, "Некорректный URL")
                    .get();
            var verifyUrl = getVerifyUrl(urlName);
            if (UrlRepository.findByName(verifyUrl).isPresent()) {
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.sessionAttribute("flash-type", "info");
                //ctx.sessionAttribute("flash-type", "warning");
                //ctx.sessionAttribute("flash-type", "dismissible");
            } else {
                var url = new Url(getVerifyUrl(verifyUrl));
                UrlRepository.save(url);
                ctx.sessionAttribute("flash", "Страница успешно добавлена");
                ctx.sessionAttribute("flash-type", "success");
            }
            ctx.consumeSessionAttribute("url-value");
            ctx.redirect(NamedRoutes.urlsPath());
        } catch (ValidationException e) {
            //ctx.sessionAttribute("flash", "Некорректный URL");
            var errorMessage = e.getErrors().entrySet().stream().findFirst()
                    .get().getValue().get(0).getMessage();
            ctx.sessionAttribute("flash", errorMessage);
            ctx.sessionAttribute("flash-type", "warning");
            ctx.sessionAttribute("url-value", ctx.formParam("url"));
            ctx.redirect(NamedRoutes.rootPath());
            /*var page = new MainPage(ctx.formParam("url"));
            page.setFlash(ctx.consumeSessionAttribute("flash"));
            page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
            ctx.render("index.jte", model("page", page)).status(422);*/
        }
    }

    public static void index(Context ctx) throws SQLException {
        var page = new UrlsPage(UrlRepository.getEntities());
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Site not found"));
        var page = new UrlPage(url);
        ctx.render("urls/show.jte", model("page", page));
    }


}
