package cinescout.screenplay.domain.model

sealed interface ScreenplayIds {

    val tmdb: TmdbScreenplayId
    val trakt: TraktScreenplayId

    data class Movie(
        override val tmdb: TmdbScreenplayId.Movie,
        override val trakt: TraktScreenplayId.Movie
    ) : ScreenplayIds

    data class TvShow(
        override val tmdb: TmdbScreenplayId.TvShow,
        override val trakt: TraktScreenplayId.TvShow
    ) : ScreenplayIds
}

fun ScreenplayIds(tmdb: TmdbScreenplayId, trakt: TraktScreenplayId) = when (tmdb) {
    is TmdbScreenplayId.Movie -> ScreenplayIds.Movie(tmdb, trakt as TraktScreenplayId.Movie)
    is TmdbScreenplayId.TvShow -> ScreenplayIds.TvShow(tmdb, trakt as TraktScreenplayId.TvShow)
}
