package cinescout.movies.data.remote.tmdb

import arrow.core.Either
import cinescout.auth.tmdb.domain.usecase.CallWithTmdbAccount
import cinescout.common.model.Rating
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.movies.data.remote.TmdbRemoteMovieDataSource
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieCreditsMapper
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieImagesMapper
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieKeywordMapper
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieMapper
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieVideosMapper
import cinescout.movies.data.remote.tmdb.model.PostRating
import cinescout.movies.data.remote.tmdb.service.TmdbMovieService
import cinescout.movies.data.remote.tmdb.service.TmdbSearchService
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieImages
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.MovieVideos
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import store.PagedData
import store.Paging
import store.builder.toPagedData

internal class RealTmdbMovieDataSource(
    private val callWithTmdbAccount: CallWithTmdbAccount,
    private val movieCreditsMapper: TmdbMovieCreditsMapper,
    private val movieKeywordMapper: TmdbMovieKeywordMapper,
    private val movieMapper: TmdbMovieMapper,
    private val movieImagesMapper: TmdbMovieImagesMapper,
    private val movieService: TmdbMovieService,
    private val movieVideosMapper: TmdbMovieVideosMapper,
    private val searchService: TmdbSearchService
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

    override suspend fun getMovieImages(movieId: TmdbMovieId): Either<NetworkError, MovieImages> =
        movieService.getMovieImages(movieId)
            .map { tmdbMovieImages -> movieImagesMapper.toMovieImages(tmdbMovieImages) }

    override suspend fun getMovieVideos(movieId: TmdbMovieId): Either<NetworkError, MovieVideos> =
        movieService.getMovieVideos(movieId)
            .map { tmdbMovieVideos -> movieVideosMapper.toMovieVideos(tmdbMovieVideos) }

    override suspend fun getRatedMovies(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<MovieWithPersonalRating, Paging.Page.SingleSource>> =
        callWithTmdbAccount {
            movieService.getRatedMovies(page).map { response ->
                movieMapper.toMoviesWithRating(response)
                    .toPagedData(Paging.Page(response.page, response.totalPages))
            }
        }

    override suspend fun getRecommendationsFor(
        movieId: TmdbMovieId,
        page: Int
    ): Either<NetworkError, PagedData.Remote<Movie, Paging.Page.SingleSource>> =
        movieService.getRecommendationsFor(movieId, page).map { response ->
            movieMapper.toMovies(response.tmdbMovies())
                .toPagedData(Paging.Page(response.page, response.totalPages))
        }

    override suspend fun getWatchlistMovies(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<Movie, Paging.Page.SingleSource>> =
        callWithTmdbAccount {
            movieService.getMovieWatchlist(page).map { response ->
                movieMapper.toMovies(response)
                    .toPagedData(Paging.Page(response.page, response.totalPages))
            }
        }

    override suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit> =
        movieService.postRating(movieId, PostRating.Request(rating.value))

    override suspend fun postAddToWatchlist(id: TmdbMovieId): Either<NetworkError, Unit> =
        movieService.postToWatchlist(id, shouldBeInWatchlist = true)

    override suspend fun postRemoveFromWatchlist(id: TmdbMovieId): Either<NetworkError, Unit> =
        movieService.postToWatchlist(id, shouldBeInWatchlist = false)

    override suspend fun searchMovie(
        query: String,
        page: Int
    ): Either<NetworkError, PagedData.Remote<Movie, Paging.Page.SingleSource>> =
        searchService.searchMovie(query, page).map { response ->
            movieMapper.toMovies(response.tmdbMovies())
                .toPagedData(Paging.Page(response.page, response.totalPages))
        }
}
