package cinescout.suggestions.domain

import arrow.core.Either
import arrow.core.Nel
import arrow.core.left
import arrow.core.right
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestionError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface SuggestionRepository {

    fun getSuggestedMovies(): Flow<Either<SuggestionError, Nel<SuggestedMovie>>>

    fun getSuggestedTvShows(): Flow<Either<SuggestionError, Nel<SuggestedTvShow>>>

    suspend fun storeSuggestedMovies(movies: Nel<SuggestedMovie>)

    suspend fun storeSuggestedTvShows(tvShows: Nel<SuggestedTvShow>)
}

class FakeSuggestionRepository(
    private val suggestedMovies: Nel<SuggestedMovie>? = null,
    private val suggestedMoviesFlow: MutableStateFlow<Either<SuggestionError, Nel<SuggestedMovie>>> =
        MutableStateFlow(suggestedMovies?.right() ?: SuggestionError.NoSuggestions.left()),
    private val suggestedTvShows: Nel<SuggestedTvShow>? = null,
    private val suggestedTvShowsFlow: MutableStateFlow<Either<SuggestionError, Nel<SuggestedTvShow>>> =
        MutableStateFlow(suggestedTvShows?.right() ?: SuggestionError.NoSuggestions.left())
) : SuggestionRepository {

    override fun getSuggestedMovies(): Flow<Either<SuggestionError, Nel<SuggestedMovie>>> =
        suggestedMoviesFlow

    override fun getSuggestedTvShows(): Flow<Either<SuggestionError, Nel<SuggestedTvShow>>> =
        suggestedTvShowsFlow

    override suspend fun storeSuggestedMovies(movies: Nel<SuggestedMovie>) {
        val allSuggestions = suggestedMoviesFlow.value.fold(
            ifLeft = { movies },
            ifRight = { it + movies }
        )
        suggestedMoviesFlow.emit(allSuggestions.right())
    }

    override suspend fun storeSuggestedTvShows(tvShows: Nel<SuggestedTvShow>) {
        val allSuggestions = suggestedTvShowsFlow.value.fold(
            ifLeft = { tvShows },
            ifRight = { it + tvShows }
        )
        suggestedTvShowsFlow.emit(allSuggestions.right())
    }
}
