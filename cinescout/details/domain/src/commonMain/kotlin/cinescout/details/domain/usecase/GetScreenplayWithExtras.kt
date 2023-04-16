package cinescout.details.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import cinescout.details.domain.model.ScreenplayWithExtras
import cinescout.error.NetworkError
import cinescout.people.domain.usecase.GetScreenplayCredits
import cinescout.rating.domain.usecase.GetPersonalRating
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.store.ScreenplayStore
import cinescout.screenplay.domain.usecase.GetScreenplayGenres
import cinescout.screenplay.domain.usecase.GetScreenplayKeywords
import cinescout.store5.ext.filterData
import cinescout.utils.kotlin.combine
import cinescout.watchlist.domain.usecase.GetIsScreenplayInWatchlist
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetScreenplayWithExtras {

    operator fun invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayWithExtras>>
}

@Factory
internal class RealGetScreenplayWithExtras(
    private val getCredits: GetScreenplayCredits,
    private val getGenres: GetScreenplayGenres,
    private val getIsInWatchlist: GetIsScreenplayInWatchlist,
    private val getKeywords: GetScreenplayKeywords,
    private val getPersonalRating: GetPersonalRating,
    private val screenplayStore: ScreenplayStore
) : GetScreenplayWithExtras {

    override fun invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayWithExtras>> = combine(
        getCredits(screenplayIds.tmdb, refresh),
        getGenres(screenplayIds.tmdb, refresh),
        getIsInWatchlist(screenplayIds.tmdb, refresh),
        getKeywords(screenplayIds.tmdb, refresh),
        getPersonalRating(screenplayIds.tmdb, refresh),
        screenplayStore.stream(StoreReadRequest.cached(screenplayIds.trakt, refresh)).filterData()
    ) { creditsEither,
        genresEither,
        isInWatchlistEither,
        keywordsEither,
        personalRatingEither,
        screenplayEither ->
        either {
            ScreenplayWithExtras.from(
                credits = creditsEither.bind(),
                genres = genresEither.bind(),
                isInWatchlist = isInWatchlistEither.bind(),
                keywords = keywordsEither.bind(),
                personalRating = personalRatingEither.bind(),
                screenplay = screenplayEither.bind()
            )
        }
    }
}
