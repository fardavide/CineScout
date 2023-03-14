package cinescout.suggestions.domain

import arrow.core.Either
import arrow.core.Nel
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.core.toNonEmptyListOrNull
import cinescout.suggestions.domain.model.SuggestedMovieId
import cinescout.suggestions.domain.model.SuggestedScreenplay
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.model.SuggestedTvShowId
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.suggestionIds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface SuggestionRepository {

    fun getSuggestedMovieIds(): Flow<Either<SuggestionError, Nel<SuggestedMovieId>>>

    fun getSuggestedTvShowIds(): Flow<Either<SuggestionError, Nel<SuggestedTvShowId>>>

    fun getSuggestionIds(): Flow<Either<SuggestionError, Nel<SuggestedScreenplayId>>>

    suspend fun storeSuggestionIds(ids: Collection<SuggestedScreenplayId>)

    suspend fun storeSuggestions(screenplays: Collection<SuggestedScreenplay>)

}

class FakeSuggestionRepository(
    private val suggestedIds: Nel<SuggestedScreenplayId>? = null,
    private val suggestedIdsFlow: MutableStateFlow<Either<SuggestionError, Nel<SuggestedScreenplayId>>> =
        MutableStateFlow(suggestedIds?.right() ?: SuggestionError.NoSuggestions.left()),
    private val suggestedScreenplays: Nel<SuggestedScreenplay>? = null,
    private val suggestedScreenplaysFlow: MutableStateFlow<Either<SuggestionError, Nel<SuggestedScreenplay>>> =
        MutableStateFlow(suggestedScreenplays?.right() ?: SuggestionError.NoSuggestions.left())
) : SuggestionRepository {

    override fun getSuggestedMovieIds(): Flow<Either<SuggestionError, Nel<SuggestedMovieId>>> =
        suggestedIdsFlow.map { either ->
            either.flatMap { list ->
                list.filterIsInstance<SuggestedMovieId>().toNonEmptyListOrSuggestionError()
            }
        }

    override fun getSuggestedTvShowIds(): Flow<Either<SuggestionError, Nel<SuggestedTvShowId>>> =
        suggestedIdsFlow.map { either ->
            either.flatMap { list ->
                list.filterIsInstance<SuggestedTvShowId>().toNonEmptyListOrSuggestionError()
            }
        }

    override fun getSuggestionIds(): Flow<Either<SuggestionError, Nel<SuggestedScreenplayId>>> =
        suggestedIdsFlow

    override suspend fun storeSuggestionIds(ids: Collection<SuggestedScreenplayId>) {
        ids.toNonEmptyListOrNull()?.let { nonEmptyList ->
            val allSuggestedIds = suggestedIdsFlow.value.fold(
                ifLeft = { nonEmptyList.map { SuggestedScreenplayId(it.screenplayId, it.source) } },
                ifRight = { prev -> prev + nonEmptyList.map { SuggestedScreenplayId(it.screenplayId, it.source) } }
            )
            suggestedIdsFlow.emit(allSuggestedIds.right())
        }
    }

    override suspend fun storeSuggestions(screenplays: Collection<SuggestedScreenplay>) {
        screenplays.toNonEmptyListOrNull()?.let { nonEmptyList ->
            val allSuggestions = suggestedScreenplaysFlow.value.fold(
                ifLeft = { nonEmptyList },
                ifRight = { prev -> prev + nonEmptyList }
            )
            suggestedScreenplaysFlow.emit(allSuggestions.right())
        }
        storeSuggestionIds(screenplays.suggestionIds())
    }

    private fun <T> Collection<T>.toNonEmptyListOrSuggestionError(): Either<SuggestionError, Nel<T>> =
        toNonEmptyListOrNull()?.right() ?: SuggestionError.NoSuggestions.left()
}
