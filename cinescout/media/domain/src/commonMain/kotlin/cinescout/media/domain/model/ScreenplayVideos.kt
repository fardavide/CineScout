package cinescout.media.domain.model

import cinescout.screenplay.domain.model.ids.TmdbMovieId
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TmdbTvShowId

sealed interface ScreenplayVideos {

    val screenplayId: TmdbScreenplayId
    val videos: List<TmdbVideo>
}

fun ScreenplayVideos(screenplayId: TmdbScreenplayId, videos: List<TmdbVideo>): ScreenplayVideos =
    when (screenplayId) {
        is TmdbMovieId -> MovieVideos(
            screenplayId = screenplayId,
            videos = videos
        )
        is TmdbTvShowId -> TvShowVideos(
            screenplayId = screenplayId,
            videos = videos
        )
    }

data class MovieVideos(
    override val screenplayId: TmdbMovieId,
    override val videos: List<TmdbVideo>
) : ScreenplayVideos

data class TvShowVideos(
    override val screenplayId: TmdbTvShowId,
    override val videos: List<TmdbVideo>
) : ScreenplayVideos
