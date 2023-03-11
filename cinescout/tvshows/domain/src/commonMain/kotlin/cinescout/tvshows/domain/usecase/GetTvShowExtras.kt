package cinescout.tvshows.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import cinescout.error.NetworkError
import cinescout.store5.ext.filterData
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowWithExtras
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Factory

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
        refresh: Boolean = true
    ): Flow<Either<NetworkError, TvShowWithExtras>> = combine(
        getIsTvShowInWatchlist(tvShowId, refresh),
        getTvShowCredits(tvShowId, refresh).filterData(),
        getTvShowDetails(tvShowId, refresh).filterData(),
        getTvShowKeywords(tvShowId, refresh).filterData(),
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

    operator fun invoke(
        tvShow: TvShow,
        refresh: Boolean = true
    ): Flow<Either<NetworkError, TvShowWithExtras>> = this(tvShow.tmdbId, refresh)
}
