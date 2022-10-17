package cinescout.tvshows.domain.model

import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage

data class TvShowImages(
    val backdrops: List<TmdbBackdropImage>,
    val posters: List<TmdbPosterImage>,
    val tvShowId: TmdbTvShowId
)
