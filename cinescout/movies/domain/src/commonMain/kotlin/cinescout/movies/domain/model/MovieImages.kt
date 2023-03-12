package cinescout.movies.domain.model

@Deprecated(
    "Use cinescout.screenplay.domain.model.MovieImages instead",
    ReplaceWith("cinescout.screenplay.domain.model.MovieImages", "cinescout.screenplay.domain.model.MovieImages")
)
typealias MovieImages = cinescout.screenplay.domain.model.MovieImages

val MovieImages.movieId: TmdbMovieId
    get() = screenplayId
