package cinescout.tvshows.domain

import arrow.core.Either
import cinescout.error.DataError
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TvShow
import cinescout.tvshows.domain.model.TmdbTvShowId
import store.PagedStore
import store.Paging
import store.Refresh

interface TvShowRepository {

    suspend fun addToDisliked(tvShowId: TmdbTvShowId)

    suspend fun addToLiked(tvShowId: TmdbTvShowId)

    suspend fun addToWatchlist(tvShowId: TmdbTvShowId): Either<DataError.Remote, Unit>

    fun getRecommendationsFor(tvShowId: TmdbTvShowId, refresh: Refresh): PagedStore<TvShow, Paging>

    suspend fun rate(tvShowId: TmdbTvShowId, rating: Rating): Either<DataError.Remote, Unit>

    suspend fun removeFromWatchlist(tvShowId: TmdbTvShowId): Either<DataError.Remote, Unit>

}
