package cinescout.movies.data.remote.tmdb

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.data.remote.TmdbRemoteMovieDataSource
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieCreditsMapper
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieMapper
import cinescout.movies.data.remote.tmdb.model.PostRating
import cinescout.movies.data.remote.tmdb.service.TmdbMovieService
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.store.PagedData
import cinescout.store.Paging
import cinescout.store.toPagedData

internal class RealTmdbMovieDataSource(
    private val movieCreditsMapper: TmdbMovieCreditsMapper,
    private val movieMapper: TmdbMovieMapper,
    private val movieService: TmdbMovieService
) : TmdbRemoteMovieDataSource {

    override suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, List<Movie>> =
        movieService.discoverMovies(params).map { response -> movieMapper.toMovies(response.tmdbMovies()) }

    override suspend fun getMovie(id: TmdbMovieId): Either<NetworkError, Movie> =
        movieService.getMovie(id).map { tmdbMovie -> movieMapper.toMovie(tmdbMovie) }

    override suspend fun getMovieCredits(movieId: TmdbMovieId): Either<NetworkError, MovieCredits> =
        movieService.getMovieCredits(movieId)
            .map { tmdbMovieCredits -> movieCreditsMapper.toMovieCredits(tmdbMovieCredits) }

    override suspend fun getRatedMovies(
        page: Int
    ): Either<NetworkError, PagedData.Remote<MovieWithPersonalRating, Paging.Page.SingleSource>> =
        movieService.getRatedMovies(page).map { response ->
            movieMapper.toMoviesWithRating(response)
                .toPagedData(Paging.Page(response.page, response.totalPages))
        }

    override suspend fun postDisliked(id: TmdbMovieId): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun postLiked(id: TmdbMovieId): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit> =
        movieService.postRating(movieId, PostRating.Request(rating.value))

    override suspend fun postAddToWatchlist(id: TmdbMovieId): Either<NetworkError, Unit> =
        movieService.postToWatchlist(id, shouldBeInWatchlist = true)

}
