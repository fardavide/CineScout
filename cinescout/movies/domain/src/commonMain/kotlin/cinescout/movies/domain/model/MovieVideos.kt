package cinescout.movies.domain.model

data class MovieVideos(
    val movieId: TmdbMovieId,
    val videos: List<TmdbVideo>
)
