package cinescout.movies.domain.model

import cinescout.screenplay.domain.model.Keyword

data class MovieKeywords(
    val movieId: TmdbMovieId,
    val keywords: List<Keyword>
)
