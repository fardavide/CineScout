package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptyList
import arrow.core.left
import arrow.core.right
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

interface GetSuggestedTvShowsWithExtras {

    operator fun invoke(
        refreshTvShowExtras: Boolean,
        take: Int = Integer.MAX_VALUE
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedTvShowWithExtras>>>
}

@Factory
class RealGetSuggestedTvShowsWithExtras(
    private val getSuggestedTvShowIds: GetSuggestedTvShowIds,
    private val getTvShowExtras: GetTvShowExtras
) : GetSuggestedTvShowsWithExtras {

    override operator fun invoke(
        refreshTvShowExtras: Boolean,
        take: Int
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedTvShowWithExtras>>> =
        getSuggestedTvShowIds().flatMapLatest { either ->
            either.fold(
                ifLeft = { suggestionError -> flowOf(suggestionError.left()) },
                ifRight = { tvShows ->
                    tvShows.take(take).map { tvShow ->
                        getTvShowExtras(tvShow.screenplayId, refresh = refreshTvShowExtras).map { tvShowExtrasEither ->
                            tvShowExtrasEither.map { tvShowExtras ->
                                SuggestedTvShowWithExtras(
                                    tvShow = tvShowExtras.tvShowWithDetails.tvShow,
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
                                ifLeft = { networkError -> SuggestionError.Source(networkError).left() },
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
        refreshTvShowExtras: Boolean,
        take: Int
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedTvShowWithExtras>>> =
        flowOf(tvShows?.right() ?: SuggestionError.NoSuggestions.left())
}
