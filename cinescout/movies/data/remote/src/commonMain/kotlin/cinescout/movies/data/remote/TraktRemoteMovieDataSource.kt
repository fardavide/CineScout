package cinescout.movies.data.remote

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.common.model.Rating
import cinescout.error.NetworkError
import cinescout.movies.data.remote.model.TraktPersonalMovieRating
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import store.PagedData
import store.Paging
import store.builder.pagedDataOf

interface TraktRemoteMovieDataSource {

    suspend fun getRatedMovies(page: Int): Either<NetworkError, PagedData.Remote<TraktPersonalMovieRating>>

    suspend fun getWatchlistMovies(page: Int): Either<NetworkError, PagedData.Remote<TmdbMovieId>>

    suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit>

    suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit>

    suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit>
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
    ): Either<NetworkError, PagedData.Remote<TraktPersonalMovieRating>> = ratedMovies
        ?.let { movies -> pagedDataOf(*movies.toTypedArray(), paging = Paging.Page.Initial).right() }
        ?: NetworkError.Unknown.left()

    override suspend fun getWatchlistMovies(page: Int): Either<NetworkError, PagedData.Remote<TmdbMovieId>> =
        watchlistMovies
            ?.let { movies -> pagedDataOf(*movies.toTypedArray(), paging = Paging.Page.Initial).right() }
            ?: NetworkError.Unknown.left()

    override suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit> {
        postRatingInvoked = true
        return Unit.right()
    }

    override suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit> {
        postAddToWatchlistInvoked = true
        return Unit.right()
    }

    override suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit> {
        postRemoveFromWatchlistInvoked = true
        return Unit.right()
    }
}
