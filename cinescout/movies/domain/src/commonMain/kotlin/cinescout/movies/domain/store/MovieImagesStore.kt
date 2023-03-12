package cinescout.movies.domain.store

import cinescout.movies.domain.model.TmdbMovieId
import cinescout.screenplay.domain.model.MovieImages
import cinescout.store5.Store5

interface MovieImagesStore : Store5<MovieImagesStoreKey, MovieImages>

@JvmInline
value class MovieImagesStoreKey(val movieId: TmdbMovieId)