package cinescout.suggestions.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.left
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.movies.domain.model.SuggestionError
import cinescout.movies.domain.usecase.GetMovieCredits
import cinescout.suggestions.domain.usecase.GetSuggestedMovies
import cinescout.suggestions.presentation.mapper.ForYouMovieUiModelMapper
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.utils.android.CineScoutViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class ForYouViewModel(
    private val forYouMovieUiModelMapper: ForYouMovieUiModelMapper,
    private val getMovieCredits: GetMovieCredits,
    private val getSuggestedMovies: GetSuggestedMovies,
    private val networkErrorMapper: NetworkErrorToMessageMapper
) : CineScoutViewModel<ForYouAction, ForYouState>(initialState = ForYouState.Loading) {

    init {
        viewModelScope.launch {
            getSuggestedMovies().flatMapLatest { listEither ->
                listEither.fold(
                    ifLeft = { error -> flowOf(error.left()) },
                    ifRight = { list ->
                        val movie = list.first()
                        getMovieCredits(movie.tmdbId).map { creditsEither ->
                            creditsEither
                                .mapLeft { SuggestionError.Source(it) }
                                .map { credits -> forYouMovieUiModelMapper.toUiModel(movie, credits) }
                        }
                    }
                )
            }.collectLatest { either ->
                val newSuggestions = either.fold(
                    ifLeft = { error -> toSuggestionsState(error) },
                    ifRight = { list -> ForYouState.SuggestedMovie.Data(list) }
                )
                updateState { currentState ->
                    currentState.copy(suggestedMovie = newSuggestions)
                }
            }
        }
    }

    override fun submit(action: ForYouAction) {
        when (action) {

        }
    }

    private fun toSuggestionsState(error: SuggestionError): ForYouState.SuggestedMovie =
        when (error) {
            is SuggestionError.Source -> {
                val message = networkErrorMapper.toMessage(error.dataError.networkError)
                ForYouState.SuggestedMovie.Error(message)
            }
            is SuggestionError.NoSuggestions -> ForYouState.SuggestedMovie.NoSuggestions
        }
}
