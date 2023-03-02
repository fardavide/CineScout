package cinescout.tvshows.domain.model

import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.screenplay.domain.model.TmdbVideo

data class TvShowMedia(
    val backdrops: List<TmdbBackdropImage>,
    val posters: List<TmdbPosterImage>,
    val videos: List<TmdbVideo>
)
