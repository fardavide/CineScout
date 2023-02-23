package cinescout.movies.data.remote

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.common.model.Rating
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.movies.data.remote.model.TraktPersonalMovieRating
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import store.PagedData
import store.Paging
import store.builder.pagedDataOf

interface TraktRemoteMovieDataSource {

    suspend fun getRatedMovies(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<TraktPersonalMovieRating, Paging.Page.SingleSource>>

    suspend fun getWatchlistMovies(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<TmdbMovieId, Paging.Page.SingleSource>>

    suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkOperation, Unit>

    suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkOperation, Unit>

    suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkOperation, Unit>
}

class FakeTraktRemoteMovieDataSource(
    ratedMovies: List<MovieWithPersonalRating>? = null,
    watchlistMovies: List<Movie>? = null
) : TraktRemoteMovieDataSource {

    var postAddToWatchlistInvoked: Boolean = false
        private set
    var postRatingInvoked: Boolean = false
        private set
    var postRemoveFromWatchlistInvoked: Boolean = false
        private set

    private val ratedMovies: List<TraktPersonalMovieRating>? = ratedMovies?.map { movieWithPersonalRating ->
        TraktPersonalMovieRating(
            tmdbId = movieWithPersonalRating.movie.tmdbId,
            rating = movieWithPersonalRating.personalRating
        )
    }

    private val watchlistMovies: List<TmdbMovieId>? = watchlistMovies?.map { movie -> movie.tmdbId }

    override suspend fun getRatedMovies(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<TraktPersonalMovieRating, Paging.Page.SingleSource>> =
        ratedMovies
            ?.let { movies -> pagedDataOf(*movies.toTypedArray(), paging = Paging.Page.SingleSource.Initial).right() }
            ?: NetworkOperation.Error(NetworkError.Unknown).left()

    override suspend fun getWatchlistMovies(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<TmdbMovieId, Paging.Page.SingleSource>> = watchlistMovies
        ?.let { movies -> pagedDataOf(*movies.toTypedArray(), paging = Paging.Page.SingleSource.Initial).right() }
        ?: NetworkOperation.Error(NetworkError.Unknown).left()

    override suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkOperation, Unit> {
        postRatingInvoked = true
        return Unit.right()
    }

    override suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkOperation, Unit> {
        postAddToWatchlistInvoked = true
        return Unit.right()
    }

    override suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkOperation, Unit> {
        postRemoveFromWatchlistInvoked = true
        return Unit.right()
    }
}
