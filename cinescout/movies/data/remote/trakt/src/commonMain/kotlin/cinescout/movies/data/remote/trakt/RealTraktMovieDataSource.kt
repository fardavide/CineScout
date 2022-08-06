package cinescout.movies.data.remote.trakt

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.data.remote.TraktRemoteMovieDataSource
import cinescout.movies.data.remote.model.TraktPersonalMovieRating
import cinescout.movies.data.remote.trakt.mapper.TraktMovieMapper
import cinescout.movies.data.remote.trakt.service.TraktMovieService
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating
import cinescout.store.PagedData
import cinescout.store.Paging

internal class RealTraktMovieDataSource(
    private val movieMapper: TraktMovieMapper,
    private val service: TraktMovieService
) : TraktRemoteMovieDataSource {

    override suspend fun getRatedMovies(
        page: Int
    ): Either<NetworkError, PagedData.Remote<TraktPersonalMovieRating, Paging.Page.SingleSource>> =
        service.getRatedMovies(page).map { pagedData ->
            pagedData.map { movie ->
                movieMapper.toMovieRating(movie)
            }
        }

    override suspend fun postRating(movie: Movie, rating: Rating) {
    }

    override suspend fun postWatchlist(movie: Movie) {
    }
}
