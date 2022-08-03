package cinescout.movies.data

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.store.PagedData

interface RemoteMovieDataSource {

    suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, List<Movie>>

    suspend fun getMovie(id: TmdbMovieId): Either<NetworkError, Movie>

    suspend fun getRatedMovies(page: Int): Either<NetworkError, PagedData.Remote<MovieWithRating>>

    suspend fun postRating(movie: Movie, rating: Rating): Either<NetworkError, Unit>

    suspend fun postWatchlist(movie: Movie)
}
