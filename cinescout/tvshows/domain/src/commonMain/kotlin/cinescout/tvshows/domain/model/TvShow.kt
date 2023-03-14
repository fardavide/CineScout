package cinescout.tvshows.domain.model

import cinescout.screenplay.domain.model.TvShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Deprecated(
    "Use cinescout.screenplay.domain.model.TvShow instead",
    ReplaceWith("cinescout.screenplay.domain.model.TvShow")
)
typealias TvShow = TvShow

fun List<TvShow>.ids(): List<TmdbTvShowId> = map { it.tmdbId }

fun Flow<List<TvShow>>.ids(): Flow<List<TmdbTvShowId>> = map { it.ids() }
