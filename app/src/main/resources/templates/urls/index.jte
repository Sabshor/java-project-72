@import hexlet.code.util.NamedRoutes
@import hexlet.code.util.Utils
@import hexlet.code.dto.urls.UrlsPage
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <section>
        <div class="container-lg mt-5">
            <h1>Сайты</h1>
            <table class="table table-bordered table-hover mt-3">
                <thead>
                <tr>
                    <th class="col-1">ID</th>
                    <th>Имя</th>
                    <th class="col-2">Последняя проверка</th>
                    <th class="col-1">Код ответа</th>
                </tr>
                </thead>
                <tbody>
                    @for(var url : page.getUrls())
                        <tr>
                            <td>
                                ${url.getId()}
                            </td>
                            <td>
                                <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
                            </td>
                            <td>
                                @if (page.getLastCheckUrls().containsKey(url.getId()))
                                    ${Utils.getFormattedData(page.getLastCheckUrls().get(url.getId()).getCreatedAt())}
                                @endif
                            </td>
                            <td>
                                @if (page.getLastCheckUrls().containsKey(url.getId()))
                                    ${page.getLastCheckUrls().get(url.getId()).getStatusCode()}
                                @endif
                            </td>
                        </tr>
                    @endfor
                </tbody>
            </table>
        </div>
    </section>
`
)