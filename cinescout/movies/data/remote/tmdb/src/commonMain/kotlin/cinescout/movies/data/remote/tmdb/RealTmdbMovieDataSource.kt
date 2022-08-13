package cinescout.movies.data.remote.tmdb

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.data.remote.TmdbRemoteMovieDataSource
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieCreditsMapper
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieKeywordMapper
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieMapper
import cinescout.movies.data.remote.tmdb.model.PostRating
import cinescout.movies.data.remote.tmdb.service.TmdbMovieService
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.store.PagedData
import cinescout.store.Paging
import cinescout.store.toPagedData

internal class RealTmdbMovieDataSource(
    private val movieCreditsMapper: TmdbMovieCreditsMapper,
    private val movieKeywordMapper: TmdbMovieKeywordMapper,
    private val movieMapper: TmdbMovieMapper,
    private val movieService: TmdbMovieService
) : TmdbRemoteMovieDataSource {

    override suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, List<Movie>> =
        movieService.discoverMovies(params).map { response -> movieMapper.toMovies(response.tmdbMovies()) }

    override suspend fun getMovieDetails(id: TmdbMovieId): Either<NetworkError, MovieWithDetails> =
        movieService.getMovieDetails(id).map { tmdbMovie -> movieMapper.toMovieWithDetails(tmdbMovie) }

    override suspend fun getMovieCredits(movieId: TmdbMovieId): Either<NetworkError, MovieCredits> =
        movieService.getMovieCredits(movieId)
            .map { tmdbMovieCredits -> movieCreditsMapper.toMovieCredits(tmdbMovieCredits) }

    override suspend fun getMovieKeywords(movieId: TmdbMovieId): Either<NetworkError, MovieKeywords> =
        movieService.getMovieKeywords(movieId)
            .map { tmdbMovieKeywords -> movieKeywordMapper.toMovieKeywords(tmdbMovieKeywords) }

    override suspend fun getRatedMovies(
        page: Int
    ): Either<NetworkError, PagedData.Remote<MovieWithPersonalRating, Paging.Page.SingleSource>> =
        movieService.getRatedMovies(page).map { response ->
            movieMapper.toMoviesWithRating(response)
                .toPagedData(Paging.Page(response.page, response.totalPages))
        }

    override suspend fun getWatchlistMovies(
        page: Int
    ): Either<NetworkError, PagedData.Remote<Movie, Paging.Page.SingleSource>> =
        movieService.getMovieWatchlist(page).map { response ->
            movieMapper.toMovies(response)
                .toPagedData(Paging.Page(response.page, response.totalPages))
        }

    override suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit> =
        movieService.postRating(movieId, PostRating.Request(rating.value))

    override suspend fun postAddToWatchlist(id: TmdbMovieId): Either<NetworkError, Unit> =
        movieService.postToWatchlist(id, shouldBeInWatchlist = true)

}
