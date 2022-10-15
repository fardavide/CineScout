package cinescout.movies.domain.model

import cinescout.common.model.Keyword

data class MovieKeywords(
    val movieId: TmdbMovieId,
    val keywords: List<Keyword>
)
