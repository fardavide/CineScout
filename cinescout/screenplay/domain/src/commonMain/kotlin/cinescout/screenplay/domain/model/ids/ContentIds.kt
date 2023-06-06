package cinescout.screenplay.domain.model.ids

import cinescout.unsupported

sealed interface ContentIds {

    val tmdb: TmdbContentId
    val trakt: TraktContentId
}

sealed interface TmdbContentId {

    val value: Int

    companion object {

        inline fun <reified T : TmdbContentId> invalid(): T = when (T::class) {
            TmdbEpisodeId::class -> TmdbEpisodeId(-1) as T
            TmdbMovieId::class -> TmdbMovieId(-1) as T
            TmdbSeasonId::class -> TmdbSeasonId(-1) as T
            TmdbTvShowId::class -> TmdbTvShowId(-1) as T
            else -> unsupported
        }
    }
}

sealed interface TraktContentId {

    val value: Int
}

fun ContentIds(tmdb: TmdbContentId, trakt: TraktContentId) = when (tmdb) {
    is TmdbEpisodeId -> EpisodeIds(tmdb, trakt as TraktEpisodeId)
    is TmdbMovieId -> MovieIds(tmdb, trakt as TraktMovieId)
    is TmdbSeasonId -> SeasonIds(tmdb, trakt as TraktSeasonId)
    is TmdbTvShowId -> TvShowIds(tmdb, trakt as TraktTvShowId)
}
