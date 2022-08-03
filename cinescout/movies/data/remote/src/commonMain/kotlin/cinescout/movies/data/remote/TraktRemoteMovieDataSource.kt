package cinescout.movies.data.remote

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieRating
import cinescout.movies.domain.model.Rating
import cinescout.store.PagedData

interface TraktRemoteMovieDataSource {

    suspend fun getRatedMovies(page: Int): Either<NetworkError, PagedData.Remote<MovieRating>>

    suspend fun postRating(movie: Movie, rating: Rating)

    suspend fun postWatchlist(movie: Movie)
}
