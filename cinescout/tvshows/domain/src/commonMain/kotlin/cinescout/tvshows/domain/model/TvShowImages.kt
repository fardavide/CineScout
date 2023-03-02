package cinescout.tvshows.domain.model

import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage

data class TvShowImages(
    val backdrops: List<TmdbBackdropImage>,
    val posters: List<TmdbPosterImage>,
    val tvShowId: TmdbTvShowId
)
