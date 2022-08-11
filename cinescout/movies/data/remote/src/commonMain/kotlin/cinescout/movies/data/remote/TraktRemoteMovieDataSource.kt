package cinescout.movies.data.remote

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.data.remote.model.TraktPersonalMovieRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.store.PagedData
import cinescout.store.Paging

interface TraktRemoteMovieDataSource {

    suspend fun getRatedMovies(
        page: Int
    ): Either<NetworkError, PagedData.Remote<TraktPersonalMovieRating, Paging.Page.SingleSource>>

    suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit>

    suspend fun postWatchlist(id: TmdbMovieId): Either<NetworkError, Unit>
}
