package cinescout.movies.domain.model

import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.screenplay.domain.model.TmdbVideo

data class MovieMedia(
    val backdrops: List<TmdbBackdropImage>,
    val posters: List<TmdbPosterImage>,
    val videos: List<TmdbVideo>
)
