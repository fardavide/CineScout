package cinescout.tvshows.domain.model

import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage
import cinescout.common.model.TmdbVideo

data class TvShowMedia(
    val backdrops: List<TmdbBackdropImage>,
    val posters: List<TmdbPosterImage>,
    val videos: List<TmdbVideo>
)
