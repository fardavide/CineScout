package cinescout.tvshows.domain.model

import cinescout.screenplay.domain.model.TmdbVideo

data class TvShowVideos(
    val tvShowId: TmdbTvShowId,
    val videos: List<TmdbVideo>
)
