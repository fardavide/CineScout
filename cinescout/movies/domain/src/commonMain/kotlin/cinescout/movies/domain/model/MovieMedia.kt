package cinescout.movies.domain.model

import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage

data class MovieMedia(
    val backdrops: List<TmdbBackdropImage>,
    val posters: List<TmdbPosterImage>,
    val videos: List<TmdbVideo>
)
