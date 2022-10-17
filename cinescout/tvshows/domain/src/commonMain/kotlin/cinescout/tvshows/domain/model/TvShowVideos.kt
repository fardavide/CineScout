package cinescout.tvshows.domain.model

import cinescout.common.model.TmdbVideo

data class TvShowVideos(
    val tvShowId: TmdbTvShowId,
    val videos: List<TmdbVideo>
)
