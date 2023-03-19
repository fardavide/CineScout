package cinescout.suggestions.data

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptyList
import arrow.core.toNonEmptyListOrNone
import arrow.core.toNonEmptyListOrNull
import cinescout.error.DataError
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedScreenplay
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.suggestionIds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface LocalSuggestionDataSource {

    fun findAllSuggestionIds(
        type: ScreenplayType
    ): Flow<Either<DataError.Local, NonEmptyList<SuggestedScreenplayId>>>

    suspend fun insertSuggestionIds(suggestions: Collection<SuggestedScreenplayId>)

    suspend fun insertSuggestedMovies(suggestedMovies: Collection<SuggestedMovie>)

    suspend fun insertSuggestedTvShows(suggestedTvShows: Collection<SuggestedTvShow>)
}

class FakeLocalSuggestionDataSource(
    suggestionIds: Nel<SuggestedScreenplayId>? = null,
    suggestions: Nel<SuggestedScreenplay>? = null
) : LocalSuggestionDataSource {

    private val mutableSuggestionIdsFlow = MutableStateFlow(suggestionIds.orEmpty())
    private val mutableSuggestionsFlow = MutableStateFlow(suggestions.orEmpty())

    override fun findAllSuggestionIds(
        type: ScreenplayType
    ): Flow<Either<DataError.Local, NonEmptyList<SuggestedScreenplayId>>> =
        mutableSuggestionIdsFlow.map { list ->
            list.toNonEmptyListOrNone()
                .toEither { DataError.Local.NoCache }
        }

    override suspend fun insertSuggestionIds(suggestions: Collection<SuggestedScreenplayId>) {
        suggestions.toNonEmptyListOrNull()?.let {
            mutableSuggestionIdsFlow.emit(mutableSuggestionIdsFlow.value + it)
        }
    }

    override suspend fun insertSuggestedMovies(suggestedMovies: Collection<SuggestedMovie>) {
        mutableSuggestionIdsFlow.emit(mutableSuggestionIdsFlow.value + suggestedMovies.suggestionIds())
        mutableSuggestionsFlow.emit(mutableSuggestionsFlow.value + suggestedMovies)
    }

    override suspend fun insertSuggestedTvShows(suggestedTvShows: Collection<SuggestedTvShow>) {
        mutableSuggestionIdsFlow.emit(mutableSuggestionIdsFlow.value + suggestedTvShows.suggestionIds())
        mutableSuggestionsFlow.emit(mutableSuggestionsFlow.value + suggestedTvShows)
    }
}
