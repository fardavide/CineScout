package cinescout.tvshows.data.remote

import arrow.core.Either
import cinescout.common.model.Rating
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.network.dualSourceCall
import cinescout.network.dualSourceCallWithResult
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.model.TvShowIdWithPersonalRating
import cinescout.tvshows.domain.model.TvShowImages
import cinescout.tvshows.domain.model.TvShowKeywords
import cinescout.tvshows.domain.model.TvShowVideos
import cinescout.tvshows.domain.model.TvShowWithDetails
import store.PagedData
import store.Paging

class RealRemoteTvShowDataSource(
    private val tmdbSource: TmdbRemoteTvShowDataSource,
    private val traktSource: TraktRemoteTvShowDataSource
) : RemoteTvShowDataSource {

    override suspend fun getRatedTvShows(
        page: Paging.Page.DualSources
    ): Either<NetworkOperation, PagedData.Remote<TvShowIdWithPersonalRating, Paging.Page.DualSources>> =
        dualSourceCallWithResult(
            page = page,
            firstSourceCall = { paging ->
                tmdbSource.getRatedTvShows(paging.page).map { data ->
                    data.map { movieWithPersonalRating ->
                        TvShowIdWithPersonalRating(
                            movieWithPersonalRating.tvShow.tmdbId,
                            movieWithPersonalRating.personalRating
                        )
                    }
                }
            },
            secondSourceCall = { paging ->
                traktSource.getRatedTvShows(paging.page).map { data ->
                    data.map { traktPersonalTvShowRating ->
                        TvShowIdWithPersonalRating(
                            traktPersonalTvShowRating.tmdbId,
                            traktPersonalTvShowRating.rating
                        )
                    }
                }
            },
            id = { movieIdWithPersonalRating -> movieIdWithPersonalRating.tvShowId }
        )

    override suspend fun getRecommendationsFor(
        tvShowId: TmdbTvShowId,
        page: Paging.Page.SingleSource
    ): Either<NetworkError, PagedData.Remote<TvShow, Paging.Page.SingleSource>> =
        tmdbSource.getRecommendationsFor(tvShowId, page.page)

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
        page: Paging.Page.DualSources
    ): Either<NetworkOperation, PagedData.Remote<TmdbTvShowId, Paging.Page.DualSources>> =
        dualSourceCallWithResult(
            page = page,
            firstSourceCall = { paging ->
                tmdbSource.getWatchlistTvShows(paging.page).map { pagedData ->
                    pagedData.map { movie -> movie.tmdbId }
                }
            },
            secondSourceCall = { paging ->
                traktSource.getWatchlistTvShows(paging.page)
            }
        )

    override suspend fun postAddToWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit> =
        dualSourceCall(
            firstSourceCall = { tmdbSource.postAddToWatchlist(tvShowId) },
            secondSourceCall = { traktSource.postAddToWatchlist(tvShowId) }
        )

    override suspend fun postRating(tvShowId: TmdbTvShowId, rating: Rating): Either<NetworkError, Unit> =
        dualSourceCall(
            firstSourceCall = { tmdbSource.postRating(tvShowId, rating) },
            secondSourceCall = { traktSource.postRating(tvShowId, rating) }
        )

    override suspend fun postRemoveFromWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit> =
        dualSourceCall(
            firstSourceCall = { tmdbSource.postRemoveFromWatchlist(tvShowId) },
            secondSourceCall = { traktSource.postRemoveFromWatchlist(tvShowId) }
        )

    override suspend fun searchTvShow(
        query: String,
        page: Paging.Page.SingleSource
    ): Either<NetworkError, PagedData.Remote<TvShow, Paging.Page.SingleSource>> =
        tmdbSource.searchTvShow(query = query, page = page.page)
}
