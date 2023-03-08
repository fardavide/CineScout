package cinescout.screenplay.domain.model

sealed interface Screenplay {

    val title: String
    val tmdbId: TmdbScreenplayId

    data class Movie(
        override val title: String,
        override val tmdbId: TmdbScreenplayId.Movie
    ) : Screenplay

    data class TvShow(
        override val title: String,
        override val tmdbId: TmdbScreenplayId.TvShow
    ) : Screenplay
}
