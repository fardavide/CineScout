package cinescout.movies.domain

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.store.PagedStore
import cinescout.store.Paging
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun addToWatchlist(movie: Movie)

    fun discoverMovies(params: DiscoverMoviesParams): Flow<Either<DataError.Remote, List<Movie>>>

    fun getAllRatedMovies(): PagedStore<MovieWithPersonalRating, Paging.Page.DualSources>

    fun getMovie(id: TmdbMovieId): Flow<Either<DataError, Movie>>

    fun getMovieCredits(movieId: TmdbMovieId): Flow<Either<DataError.Remote, MovieCredits>>

    suspend fun rate(movie: Movie, rating: Rating): Either<DataError, Unit>

    suspend fun syncRatedMovies()
}
