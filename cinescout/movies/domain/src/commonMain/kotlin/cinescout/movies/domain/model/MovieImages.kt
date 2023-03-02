package cinescout.movies.domain.model

import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage

data class MovieImages(
    val backdrops: List<TmdbBackdropImage>,
    val movieId: TmdbMovieId,
    val posters: List<TmdbPosterImage>
)
