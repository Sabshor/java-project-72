package hexlet.code.controller;

import hexlet.code.dto.MainPage;
import io.javalin.http.Context;

import static io.javalin.rendering.template.TemplateUtil.model;

public class RootController {
    public static void index(Context ctx) {
        //сохранение ошибочного значения url, введенного пользователем
        //не знаю как использовать это значение через класс, а не через куки при передаче на другую страницу?
        var page = new MainPage(ctx.consumeSessionAttribute("url-value"));

        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("index.jte", model("page", page));
        //ctx.render("index.jte");
    }
}
