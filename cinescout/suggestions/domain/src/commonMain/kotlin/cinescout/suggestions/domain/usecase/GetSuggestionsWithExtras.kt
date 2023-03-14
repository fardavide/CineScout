package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.left
import arrow.core.right
import cinescout.details.domain.usecase.GetScreenplayWithExtras
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.suggestions.domain.model.SuggestedScreenplayWithExtras
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.utils.kotlin.combineToLazyList
import cinescout.utils.kotlin.nonEmptyUnsafe
import cinescout.utils.kotlin.shiftWithAnyRight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

interface GetSuggestionsWithExtras {

    operator fun invoke(
        type: ScreenplayType,
        shouldRefreshExtras: Boolean,
        take: Int = Integer.MAX_VALUE
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedScreenplayWithExtras>>>
}

@Factory
class RealGetSuggestionsWithExtras(
    private val getSuggestionIds: GetSuggestionIds,
    private val getScreenplayWithExtras: GetScreenplayWithExtras
) : GetSuggestionsWithExtras {

    override operator fun invoke(
        type: ScreenplayType,
        shouldRefreshExtras: Boolean,
        take: Int
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedScreenplayWithExtras>>> =
        getSuggestionIds(type).flatMapLatest { either ->
            either.fold(
                ifLeft = { suggestionError -> flowOf(suggestionError.left()) },
                ifRight = { suggestionIds ->
                    suggestionIds.take(take).map { suggestionId ->
                        getScreenplayWithExtras(
                            screenplayId = suggestionId.screenplayId,
                            refresh = shouldRefreshExtras
                        ).map { screenplayWithExtrasEither ->
                            screenplayWithExtrasEither.map { screenplayWithExtras ->
                                SuggestedScreenplayWithExtras(
                                    screenplay = screenplayWithExtras.screenplay,
                                    affinity = suggestionId.affinity,
                                    genres = screenplayWithExtras.genres,
                                    credits = screenplayWithExtras.credits,
                                    isInWatchlist = screenplayWithExtras.isInWatchlist,
                                    keywords = screenplayWithExtras.keywords,
                                    personalRating = screenplayWithExtras.personalRating,
                                    source = suggestionId.source
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

class FakeGetSuggestionsWithExtras : GetSuggestionsWithExtras {

    override operator fun invoke(
        type: ScreenplayType,
        shouldRefreshExtras: Boolean,
        take: Int
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedScreenplayWithExtras>>> = TODO()
}
