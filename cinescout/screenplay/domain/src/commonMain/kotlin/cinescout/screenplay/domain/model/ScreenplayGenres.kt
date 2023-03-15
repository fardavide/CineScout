package cinescout.screenplay.domain.model

sealed interface ScreenplayGenres {

    val genres: List<Genre>
    val screenplayId: TmdbScreenplayId
}

fun ScreenplayGenres(genres: List<Genre>, screenplayId: TmdbScreenplayId) = when (screenplayId) {
    is TmdbScreenplayId.Movie -> MovieGenres(genres, screenplayId)
    is TmdbScreenplayId.TvShow -> TvShowGenres(genres, screenplayId)
}

data class MovieGenres(
    override val genres: List<Genre>,
    override val screenplayId: TmdbScreenplayId.Movie
) : ScreenplayGenres

data class TvShowGenres(
    override val genres: List<Genre>,
    override val screenplayId: TmdbScreenplayId.TvShow
) : ScreenplayGenres
