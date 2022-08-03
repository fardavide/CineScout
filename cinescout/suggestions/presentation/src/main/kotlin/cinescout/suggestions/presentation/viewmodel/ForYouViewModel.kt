package cinescout.suggestions.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.movies.domain.model.SuggestionError
import cinescout.suggestions.domain.usecase.GetSuggestedMovies
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.utils.android.CineScoutViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.time.DurationUnit.SECONDS
import kotlin.time.toDuration

internal class ForYouViewModel(
    private val getSuggestedMovies: GetSuggestedMovies,
    private val networkErrorMapper: NetworkErrorToMessageMapper
) : CineScoutViewModel<ForYouAction, ForYouState>(initialState = ForYouState.Loading) {

    init {
        viewModelScope.launch {
            getSuggestedMovies().debounce(1.toDuration(SECONDS)).collectLatest { listEither ->
                val newSuggestions = listEither.fold(
                    ifLeft = { error -> toSuggestionsState(error) },
                    ifRight = { list -> ForYouState.SuggestedMovies.Data(list) }
                )
                updateState { currentState ->
                    currentState.copy(suggestedMovies = newSuggestions)
                }
            }
        }
    }

    override fun submit(action: ForYouAction) {
        when (action) {

        }
    }

    private fun toSuggestionsState(error: SuggestionError): ForYouState.SuggestedMovies =
        when (error) {
            is SuggestionError.Source -> {
                val message = networkErrorMapper.toMessage(error.dataError.networkError)
                ForYouState.SuggestedMovies.Error(message)
            }
            is SuggestionError.NoSuggestions -> ForYouState.SuggestedMovies.NoSuggestions
        }
}
