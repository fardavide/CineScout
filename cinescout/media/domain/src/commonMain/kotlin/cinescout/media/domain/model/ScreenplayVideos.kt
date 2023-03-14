package cinescout.media.domain.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

sealed interface ScreenplayVideos {

    val screenplayId: TmdbScreenplayId
    val videos: List<TmdbVideo>
}

fun ScreenplayVideos(screenplayId: TmdbScreenplayId, videos: List<TmdbVideo>): ScreenplayVideos =
    when (screenplayId) {
        is TmdbScreenplayId.Movie -> MovieVideos(
            screenplayId = screenplayId,
            videos = videos
        )
        is TmdbScreenplayId.TvShow -> TvShowVideos(
            screenplayId = screenplayId,
            videos = videos
        )
    }

data class MovieVideos(
    override val screenplayId: TmdbScreenplayId.Movie,
    override val videos: List<TmdbVideo>
) : ScreenplayVideos

data class TvShowVideos(
    override val screenplayId: TmdbScreenplayId.TvShow,
    override val videos: List<TmdbVideo>
) : ScreenplayVideos
