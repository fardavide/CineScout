package cinescout.movies.data.remote.trakt

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.data.remote.TraktRemoteMovieDataSource
import cinescout.movies.data.remote.model.TraktPersonalMovieRating
import cinescout.movies.data.remote.trakt.mapper.TraktMovieMapper
import cinescout.movies.data.remote.trakt.service.TraktMovieService
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.screenplay.domain.model.Rating
import org.koin.core.annotation.Factory

@Factory
internal class RealTraktMovieDataSource(
    private val movieMapper: TraktMovieMapper,
    private val service: TraktMovieService
) : TraktRemoteMovieDataSource {

    override suspend fun getPersonalRecommendations(): Either<NetworkError, List<TmdbMovieId>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRatedMovies(): Either<NetworkError, List<TraktPersonalMovieRating>> =
        service.getRatedMovies().map { list ->
            list.map { movie ->
                movieMapper.toMovieRating(movie)
            }
        }

    override suspend fun getWatchlistMovies(): Either<NetworkError, List<TmdbMovieId>> =
        service.getWatchlistMovies().map { list ->
            list.map { movie ->
                movie.movie.ids.tmdb
            }
        }

    override suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit> =
        service.postRating(movieId, rating)

    override suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit> =
        service.postAddToWatchlist(movieId)

    override suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit> =
        service.postRemoveFromWatchlist(movieId)

}
