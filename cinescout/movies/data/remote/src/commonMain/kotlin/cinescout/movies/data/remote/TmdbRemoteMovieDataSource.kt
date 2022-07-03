package cinescout.movies.data.remote

import arrow.core.Either
import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.data.remote.model.TmdbMovieId
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating
import cinescout.network.NetworkError

interface TmdbRemoteMovieDataSource {

    suspend fun getMovie(id: TmdbMovieId): Either<NetworkError, TmdbMovie>

    suspend fun postRating(movie: Movie, rating: Rating)

    suspend fun postWatchlist(movie: Movie)
}
