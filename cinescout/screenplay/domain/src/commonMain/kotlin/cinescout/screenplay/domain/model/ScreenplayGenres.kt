package cinescout.screenplay.domain.model

import cinescout.screenplay.domain.model.ids.TmdbMovieId
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TmdbTvShowId

sealed interface ScreenplayGenres {

    val genres: List<Genre>
    val screenplayId: TmdbScreenplayId
}

fun ScreenplayGenres(genres: List<Genre>, screenplayId: TmdbScreenplayId) = when (screenplayId) {
    is TmdbMovieId -> MovieGenres(genres, screenplayId)
    is TmdbTvShowId -> TvShowGenres(genres, screenplayId)
}

data class MovieGenres(
    override val genres: List<Genre>,
    override val screenplayId: TmdbMovieId
) : ScreenplayGenres

data class TvShowGenres(
    override val genres: List<Genre>,
    override val screenplayId: TmdbTvShowId
) : ScreenplayGenres
