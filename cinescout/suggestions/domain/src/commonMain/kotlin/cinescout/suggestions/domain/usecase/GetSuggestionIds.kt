package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import cinescout.rating.domain.usecase.GetPersonalRatingIds
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.store5.ext.filterData
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.repository.SuggestionRepository
import cinescout.suggestions.domain.usecase.GetSuggestionIds.Companion.DefaultMinimumSuggestions
import cinescout.suggestions.domain.usecase.GetSuggestionIds.Companion.UpdateIfSuggestionsLessThanName
import cinescout.voting.domain.usecase.GetAllLikedScreenplays
import cinescout.watchlist.domain.usecase.GetWatchlistIds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.transformLatest
import org.jetbrains.annotations.VisibleForTesting
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

interface GetSuggestionIds {

    operator fun invoke(
        type: ScreenplayTypeFilter
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedScreenplayId>>>

    companion object {

        const val DefaultMinimumSuggestions = 20
        const val UpdateIfSuggestionsLessThanName = "updateIfSuggestionsLessThan"
    }
}

@Factory
internal class RealGetSuggestionIds(
    private val getAllLikedScreenplays: GetAllLikedScreenplays,
    private val getPersonalRatingIds: GetPersonalRatingIds,
    private val getWatchlistIds: GetWatchlistIds,
    private val suggestionRepository: SuggestionRepository,
    private val updateSuggestions: UpdateSuggestions,
    @Named(UpdateIfSuggestionsLessThanName)
    private val updateIfSuggestionsLessThan: Int = DefaultMinimumSuggestions
) : GetSuggestionIds {

    override operator fun invoke(
        type: ScreenplayTypeFilter
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedScreenplayId>>> =
        updateSuggestionsTrigger(type).flatMapLatest {
            suggestionRepository.getSuggestionIds(type).transformLatest { either ->
                emit(either)
                either
                    .onRight { ids ->
                        if (ids.size < updateIfSuggestionsLessThan) {
                            updateSuggestions(type, SuggestionsMode.Quick)
                                .onLeft { error -> emit(SuggestionError.Source(error).left()) }
                        }
                    }
                    .onLeft {
                        updateSuggestions(type, SuggestionsMode.Quick)
                            .onLeft { error -> emit(SuggestionError.Source(error).left()) }
                    }
            }
        }

    private fun updateSuggestionsTrigger(type: ScreenplayTypeFilter) = combine(
        getAllLikedScreenplays(type),
        getPersonalRatingIds(type, refresh = false).filterData(),
        getWatchlistIds(type, refresh = false).filterData()
    ) { liked, ratedEither, watchlistEither ->
        either {
            liked.isNotEmpty() ||
                ratedEither.bind().isNotEmpty() ||
                watchlistEither.bind().isNotEmpty()
        }.fold(
            ifLeft = { false },
            ifRight = { it }
        )
    }.distinctUntilChanged()
}

@VisibleForTesting
class FakeGetSuggestionIds(
    private val suggestions: NonEmptyList<SuggestedScreenplayId>? = null
) : GetSuggestionIds {

    override operator fun invoke(
        type: ScreenplayTypeFilter
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedScreenplayId>>> =
        flowOf(suggestions?.right() ?: SuggestionError.NoSuggestions.left())
}
