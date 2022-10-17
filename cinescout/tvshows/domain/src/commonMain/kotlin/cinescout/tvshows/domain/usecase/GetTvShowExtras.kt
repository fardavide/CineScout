package cinescout.tvshows.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import cinescout.error.DataError
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowWithExtras
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import store.Refresh

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
    ): Flow<Either<DataError, TvShowWithExtras>> =
        combine(
            getIsTvShowInWatchlist(tvShowId, refresh),
            getTvShowCredits(tvShowId, refresh),
            getTvShowDetails(tvShowId, refresh),
            getTvShowKeywords(tvShowId, refresh),
            getTvShowPersonalRating(tvShowId, refresh)
        ) { isInWatchlistEither, creditsEither, detailsEither, keywordsEither, personalRatingEither ->
            either {
                TvShowWithExtras(
                    tvShowWithDetails = detailsEither.bind(),
                    isInWatchlist = isInWatchlistEither.bind(),
                    credits = creditsEither.bind(),
                    keywords = keywordsEither.bind(),
                    personalRating = personalRatingEither.bind()
                )
            }
        }
}
