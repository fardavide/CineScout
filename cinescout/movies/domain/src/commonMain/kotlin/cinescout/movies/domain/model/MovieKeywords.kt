package cinescout.movies.domain.model

data class MovieKeywords(
    val movieId: TmdbMovieId,
    val keywords: List<Keyword>
)
