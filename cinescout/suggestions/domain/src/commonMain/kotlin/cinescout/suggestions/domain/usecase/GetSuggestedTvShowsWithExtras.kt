package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptyList
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.suggestions.domain.model.SuggestedTvShowWithExtras
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.tvshows.domain.usecase.GetTvShowExtras
import cinescout.utils.kotlin.combineToLazyList
import cinescout.utils.kotlin.nonEmptyUnsafe
import cinescout.utils.kotlin.shiftWithAnyRight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import store.Refresh

interface GetSuggestedTvShowsWithExtras {

    operator fun invoke(
        tvShowExtraRefresh: Refresh = Refresh.IfExpired(),
        take: Int = Integer.MAX_VALUE
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedTvShowWithExtras>>>
}

@Factory
class RealGetSuggestedTvShowsWithExtras(
    private val getSuggestedTvShows: GetSuggestedTvShows,
    private val getTvShowExtras: GetTvShowExtras
) : GetSuggestedTvShowsWithExtras {

    override operator fun invoke(
        tvShowExtraRefresh: Refresh,
        take: Int
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedTvShowWithExtras>>> =
        getSuggestedTvShows().flatMapLatest { either ->
            either.fold(
                ifLeft = { suggestionError -> flowOf(suggestionError.left()) },
                ifRight = { tvShows ->
                    tvShows.take(take).map { tvShow ->
                        getTvShowExtras(tvShow.tvShow, refresh = tvShowExtraRefresh).map { tvShowExtrasEither ->
                            tvShowExtrasEither.map { tvShowExtras ->
                                SuggestedTvShowWithExtras(
                                    tvShow = tvShow.tvShow,
                                    affinity = tvShow.affinity,
                                    genres = tvShowExtras.tvShowWithDetails.genres,
                                    credits = tvShowExtras.credits,
                                    isInWatchlist = tvShowExtras.isInWatchlist,
                                    keywords = tvShowExtras.keywords,
                                    personalRating = tvShowExtras.personalRating,
                                    source = tvShow.source
                                )
                            }
                        }
                    }
                        .combineToLazyList()
                        .map { either ->
                            either.shiftWithAnyRight().fold(
                                ifLeft = { dataError -> SuggestionError.Source(dataError as DataError.Remote).left() },
                                ifRight = { it.nonEmptyUnsafe().right() }
                            )
                        }
                }
            )
        }
}

class FakeGetSuggestedTvShowsWithExtras(
    private val tvShows: Nel<SuggestedTvShowWithExtras>? = null
) : GetSuggestedTvShowsWithExtras {

    override operator fun invoke(
        tvShowExtraRefresh: Refresh,
        take: Int
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedTvShowWithExtras>>> =
        flowOf(tvShows?.right() ?: SuggestionError.NoSuggestions.left())
}
