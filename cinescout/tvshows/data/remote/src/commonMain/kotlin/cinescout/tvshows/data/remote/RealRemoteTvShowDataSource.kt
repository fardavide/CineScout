package cinescout.tvshows.data.remote

import arrow.core.Either
import cinescout.auth.domain.usecase.CallWithCurrentUser
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.Rating
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.model.TvShowIdWithPersonalRating
import cinescout.tvshows.domain.model.TvShowImages
import cinescout.tvshows.domain.model.TvShowKeywords
import cinescout.tvshows.domain.model.TvShowVideos
import cinescout.tvshows.domain.model.TvShowWithDetails
import org.koin.core.annotation.Factory
import store.PagedData
import store.Paging

@Factory
class RealRemoteTvShowDataSource(
    private val callWithCurrentUser: CallWithCurrentUser,
    private val tmdbSource: TmdbRemoteTvShowDataSource,
    private val traktSource: TraktRemoteTvShowDataSource
) : RemoteTvShowDataSource {

    override suspend fun getRatedTvShows(
        page: Paging.Page
    ): Either<NetworkOperation, PagedData.Remote<TvShowIdWithPersonalRating>> = callWithCurrentUser.forResult(
        tmdbCall = {
            tmdbSource.getRatedTvShows(page.page).map { data ->
                data.map { movieWithPersonalRating ->
                    TvShowIdWithPersonalRating(
                        movieWithPersonalRating.tvShow.tmdbId,
                        movieWithPersonalRating.personalRating
                    )
                }
            }
        },
        traktCall = {
            traktSource.getRatedTvShows(page.page).map { data ->
                data.map { traktPersonalTvShowRating ->
                    TvShowIdWithPersonalRating(
                        traktPersonalTvShowRating.tmdbId,
                        traktPersonalTvShowRating.rating
                    )
                }
            }
        }
    )

    override suspend fun getRecommendationsFor(
        tvShowId: TmdbTvShowId,
        page: Paging.Page
    ): Either<NetworkError, PagedData.Remote<TvShow>> = tmdbSource.getRecommendationsFor(tvShowId, page.page)

    override suspend fun getTvShowCredits(movieId: TmdbTvShowId): Either<NetworkError, TvShowCredits> =
        tmdbSource.getTvShowCredits(movieId)

    override suspend fun getTvShowDetails(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowWithDetails> =
        tmdbSource.getTvShowDetails(tvShowId)

    override suspend fun getTvShowImages(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowImages> =
        tmdbSource.getTvShowImages(tvShowId)

    override suspend fun getTvShowKeywords(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowKeywords> =
        tmdbSource.getTvShowKeywords(tvShowId)

    override suspend fun getTvShowVideos(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowVideos> =
        tmdbSource.getTvShowVideos(tvShowId)

    override suspend fun getWatchlistTvShows(
        page: Paging.Page
    ): Either<NetworkOperation, PagedData.Remote<TmdbTvShowId>> = callWithCurrentUser.forResult(
        tmdbCall = {
            tmdbSource.getWatchlistTvShows(page.page).map { pagedData ->
                pagedData.map { movie -> movie.tmdbId }
            }
        },
        traktCall = {
            traktSource.getWatchlistTvShows(page.page)
        }
    )

    override suspend fun postAddToWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit> =
        callWithCurrentUser.forUnit(
            tmdbCall = { tmdbSource.postAddToWatchlist(tvShowId) },
            traktCall = { traktSource.postAddToWatchlist(tvShowId) }
        )

    override suspend fun postRating(tvShowId: TmdbTvShowId, rating: Rating): Either<NetworkError, Unit> =
        callWithCurrentUser.forUnit(
            tmdbCall = { tmdbSource.postRating(tvShowId, rating) },
            traktCall = { traktSource.postRating(tvShowId, rating) }
        )

    override suspend fun postRemoveFromWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit> =
        callWithCurrentUser.forUnit(
            tmdbCall = { tmdbSource.postRemoveFromWatchlist(tvShowId) },
            traktCall = { traktSource.postRemoveFromWatchlist(tvShowId) }
        )

    override suspend fun searchTvShow(
        query: String,
        page: Paging.Page
    ): Either<NetworkError, PagedData.Remote<TvShow>> =
        tmdbSource.searchTvShow(query = query, page = page.page)
}
