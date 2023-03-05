package cinescout.tvshows.data.remote

import arrow.core.Either
import cinescout.auth.domain.usecase.CallWithTraktAccount
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
    private val callWithTraktAccount: CallWithTraktAccount,
    private val tmdbSource: TmdbRemoteTvShowDataSource,
    private val traktSource: TraktRemoteTvShowDataSource
) : RemoteTvShowDataSource {

    override suspend fun getRatedTvShows(
        page: Paging.Page
    ): Either<NetworkOperation, PagedData.Remote<TvShowIdWithPersonalRating>> =
        callWithTraktAccount.forResult {
            traktSource.getRatedTvShows(page.page).map { data ->
                data.map { traktPersonalTvShowRating ->
                    TvShowIdWithPersonalRating(
                        traktPersonalTvShowRating.tmdbId,
                        traktPersonalTvShowRating.rating
                    )
                }
            }
        }

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
    ): Either<NetworkOperation, PagedData.Remote<TmdbTvShowId>> = callWithTraktAccount.forResult {
        traktSource.getWatchlistTvShows(page.page)
    }

    override suspend fun postAddToWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit> =
        callWithTraktAccount.forUnit { traktSource.postAddToWatchlist(tvShowId) }

    override suspend fun postRating(tvShowId: TmdbTvShowId, rating: Rating): Either<NetworkError, Unit> =
        callWithTraktAccount.forUnit { traktSource.postRating(tvShowId, rating) }

    override suspend fun postRemoveFromWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit> =
        callWithTraktAccount.forUnit { traktSource.postRemoveFromWatchlist(tvShowId) }

    override suspend fun searchTvShow(
        query: String,
        page: Paging.Page
    ): Either<NetworkError, PagedData.Remote<TvShow>> =
        tmdbSource.searchTvShow(query = query, page = page.page)
}
