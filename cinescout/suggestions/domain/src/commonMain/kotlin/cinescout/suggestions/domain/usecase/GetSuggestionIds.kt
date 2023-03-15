package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.continuations.either
import arrow.core.left
import cinescout.rating.domain.usecase.GetPersonalRatingIds
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.store5.ext.filterData
import cinescout.suggestions.domain.SuggestionRepository
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.voting.domain.usecase.GetAllLikedScreenplays
import cinescout.watchlist.domain.usecase.GetWatchlistIds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transformLatest
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
class GetSuggestionIds(
    private val getAllLikedScreenplays: GetAllLikedScreenplays,
    private val getPersonalRatingIds: GetPersonalRatingIds,
    private val getWatchlistIds: GetWatchlistIds,
    private val suggestionRepository: SuggestionRepository,
    private val updateSuggestions: UpdateSuggestions,
    @Named(UpdateIfSuggestionsLessThanName)
    private val updateIfSuggestionsLessThan: Int = DefaultMinimumSuggestions
) {

    operator fun invoke(
        type: ScreenplayType
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedScreenplayId>>> =
        updateSuggestionsTrigger(type).flatMapLatest {
            suggestionRepository.getSuggestionIds().transformLatest { either ->
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

    private fun updateSuggestionsTrigger(type: ScreenplayType) = combine(
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

    companion object {

        const val DefaultMinimumSuggestions = 20
        const val UpdateIfSuggestionsLessThanName = "updateIfSuggestionsLessThan"
    }
}
