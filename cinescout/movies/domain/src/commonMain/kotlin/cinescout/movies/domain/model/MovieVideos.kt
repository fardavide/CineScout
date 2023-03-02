package cinescout.movies.domain.model

import cinescout.screenplay.domain.model.TmdbVideo

data class MovieVideos(
    val movieId: TmdbMovieId,
    val videos: List<TmdbVideo>
)
