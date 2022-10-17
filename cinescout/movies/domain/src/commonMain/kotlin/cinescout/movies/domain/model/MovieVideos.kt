package cinescout.movies.domain.model

import cinescout.common.model.TmdbVideo

data class MovieVideos(
    val movieId: TmdbMovieId,
    val videos: List<TmdbVideo>
)
