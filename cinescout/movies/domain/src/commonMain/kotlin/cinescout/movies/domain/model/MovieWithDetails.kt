package cinescout.movies.domain.model

data class MovieWithDetails(
    val movie: Movie,
    val genres: List<Genre>
)
