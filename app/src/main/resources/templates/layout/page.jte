@import hexlet.code.dto.BasePage
@import gg.jte.Content
@param Content content
@param BasePage page = null

<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <title>Анализатор страниц</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    </head>
    <body class="d-flex flex-column min-vh-100">
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="/">Анализатор страниц</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <div class="navbar-nav">
                        <a class="nav-link" href="/">Главная</a>
                        <a class="nav-link" href="/urls">Сайты</a>
                    </div>
                </div>
            </div>
        </nav>
        <main class="flex-grow-1">
            @if(page != null && page.getFlash() != null)
                <div class="rounded-0 m-0 alert alert-dismissible fade show alert-${page.getFlashType()}" role="alert">
                    <p class="m-0">${page.getFlash()}</p>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            @endif

            ${content}

        </main>
        <footer class="footer border-top py-3 mt-5 bg-light">
            <div class="container-xl">
                <div class="text-center">
                    Link to GitHub
                    <a href="https://github.com/Sabshor" target="_blank">Click Here</a>
                </div>
            </div>
        </footer>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    </body>
</html>