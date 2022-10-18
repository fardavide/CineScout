package cinescout.movies.data.remote

import arrow.core.Either
import cinescout.common.model.Rating
import cinescout.model.NetworkOperation
import cinescout.movies.data.remote.model.TraktPersonalMovieRating
import cinescout.movies.domain.model.TmdbMovieId
import store.PagedData
import store.Paging

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
