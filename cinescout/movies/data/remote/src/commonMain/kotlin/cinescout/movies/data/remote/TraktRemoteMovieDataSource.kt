package cinescout.movies.data.remote

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieRating
import cinescout.movies.domain.model.Rating
import cinescout.store.PagedData
import cinescout.store.Paging

interface TraktRemoteMovieDataSource {

    suspend fun getRatedMovies(
        page: Int
    ): Either<NetworkError, PagedData.Remote<MovieRating, Paging.Page.SingleSource>>

    suspend fun postRating(movie: Movie, rating: Rating)

    suspend fun postWatchlist(movie: Movie)
}
