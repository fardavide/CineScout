package cinescout.movies.domain.model

data class MovieImages(
    val backdrops: List<TmdbBackdropImage>,
    val movieId: TmdbMovieId,
    val posters: List<TmdbPosterImage>
)
