package cinescout.movies.domain.store

import cinescout.movies.domain.model.MovieVideos
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.store5.Store5

interface MovieVideosStore : Store5<MovieVideosStoreKey, MovieVideos>

@JvmInline
value class MovieVideosStoreKey(val movieId: TmdbMovieId)
