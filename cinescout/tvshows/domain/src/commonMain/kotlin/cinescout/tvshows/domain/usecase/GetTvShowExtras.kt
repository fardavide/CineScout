package cinescout.tvshows.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import cinescout.error.DataError
import cinescout.store5.ext.filterData
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowWithExtras
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Factory
import store.Refresh

@Factory
class GetTvShowExtras(
    private val getIsTvShowInWatchlist: GetIsTvShowInWatchlist,
    private val getTvShowCredits: GetTvShowCredits,
    private val getTvShowDetails: GetTvShowDetails,
    private val getTvShowKeywords: GetTvShowKeywords,
    private val getTvShowPersonalRating: GetTvShowPersonalRating
) {

    operator fun invoke(
        tvShowId: TmdbTvShowId,
        refresh: Refresh = Refresh.IfExpired()
    ): Flow<Either<DataError, TvShowWithExtras>> = combine(
        getIsTvShowInWatchlist(tvShowId, refresh.toBoolean()),
        getTvShowCredits(tvShowId, refresh),
        getTvShowDetails(tvShowId, refresh.toBoolean()).filterData(),
        getTvShowKeywords(tvShowId, refresh),
        getTvShowPersonalRating(tvShowId, refresh.toBoolean())
    ) { isInWatchlistEither, creditsEither, detailsEither, keywordsEither, personalRatingEither ->
        either {
            TvShowWithExtras(
                tvShowWithDetails = detailsEither.mapLeft(DataError::Remote).bind(),
                isInWatchlist = isInWatchlistEither.mapLeft(DataError::Remote).bind(),
                credits = creditsEither.bind(),
                keywords = keywordsEither.bind(),
                personalRating = personalRatingEither.mapLeft(DataError::Remote).bind()
            )
        }
    }

    operator fun invoke(
        tvShow: TvShow,
        refresh: Refresh = Refresh.Once
    ): Flow<Either<DataError, TvShowWithExtras>> = this(tvShow.tmdbId, refresh)
}
