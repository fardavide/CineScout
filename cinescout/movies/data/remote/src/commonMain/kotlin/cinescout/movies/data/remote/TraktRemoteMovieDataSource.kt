package cinescout.movies.data.remote

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.movies.data.remote.model.TraktPersonalMovieRating
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.screenplay.domain.model.Rating

interface TraktRemoteMovieDataSource {

    suspend fun getPersonalRecommendations(): Either<NetworkError, List<TmdbMovieId>>

    suspend fun getRatedMovies(): Either<NetworkError, List<TraktPersonalMovieRating>>

    suspend fun getWatchlistMovies(): Either<NetworkError, List<TmdbMovieId>>

    suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit>

    suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit>

    suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit>
}

class FakeTraktRemoteMovieDataSource(
    private val personalRecommendations: List<TmdbMovieId>? = null,
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

    override suspend fun getPersonalRecommendations(): Either<NetworkError, List<TmdbMovieId>> =
        personalRecommendations?.right() ?: NetworkError.Unknown.left()

    override suspend fun getRatedMovies(): Either<NetworkError, List<TraktPersonalMovieRating>> =
        ratedMovies?.right() ?: NetworkError.Unknown.left()

    override suspend fun getWatchlistMovies(): Either<NetworkError, List<TmdbMovieId>> =
        watchlistMovies?.right() ?: NetworkError.Unknown.left()

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
