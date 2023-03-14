package cinescout.screenplay.domain.model

import arrow.core.NonEmptyList

sealed interface ScreenplayGenres {

    val genres: NonEmptyList<Genre>
    val screenplayId: TmdbScreenplayId
}


data class MovieGenres(
    override val genres: NonEmptyList<Genre>,
    override val screenplayId: TmdbScreenplayId.Movie
) : ScreenplayGenres

data class TvShowGenres(
    override val genres: NonEmptyList<Genre>,
    override val screenplayId: TmdbScreenplayId.TvShow
) : ScreenplayGenres
