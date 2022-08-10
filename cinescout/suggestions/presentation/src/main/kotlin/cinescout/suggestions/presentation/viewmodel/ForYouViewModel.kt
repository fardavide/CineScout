package cinescout.suggestions.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.movies.domain.model.SuggestionError
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.usecase.AddMovieToDislikedList
import cinescout.movies.domain.usecase.AddMovieToLikedList
import cinescout.movies.domain.usecase.AddMovieToWatchlist
import cinescout.movies.domain.usecase.GetMovieCredits
import cinescout.suggestions.domain.usecase.GetSuggestedMovies
import cinescout.suggestions.presentation.mapper.ForYouMovieUiModelMapper
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouMovieUiModel
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.util.FixedSizeStack
import cinescout.suggestions.presentation.util.join
import cinescout.suggestions.presentation.util.pop
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.kotlin.exhaustive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class ForYouViewModel(
    private val addMovieToDislikedList: AddMovieToDislikedList,
    private val addMovieToLikedList: AddMovieToLikedList,
    private val addMovieToWatchlist: AddMovieToWatchlist,
    private val forYouMovieUiModelMapper: ForYouMovieUiModelMapper,
    private val getMovieCredits: GetMovieCredits,
    private val getSuggestedMovies: GetSuggestedMovies,
    private val networkErrorMapper: NetworkErrorToMessageMapper
) : CineScoutViewModel<ForYouAction, ForYouState>(initialState = ForYouState.Loading) {

    private val suggestionsStack: MutableStateFlow<FixedSizeStack<ForYouMovieUiModel>> =
        MutableStateFlow(FixedSizeStack.empty(size = 10))

    init {
        viewModelScope.launch {
            suggestionsStack.collectLatest { stack ->
                val movie = stack.head()
                updateState { currentState ->
                    val suggestedMovie = when (movie) {
                        null -> ForYouState.SuggestedMovie.Loading
                        else -> ForYouState.SuggestedMovie.Data(movie = movie)
                    }
                    currentState.copy(suggestedMovie = suggestedMovie)
                }
            }
        }

        viewModelScope.launch {
            suggestionsStack
                .filterNot { it.isFull() }
                .flatMapLatest { getSuggestedMovies() }
                .flatMapLatest { listEither ->
                    listEither.fold(
                        ifLeft = { error -> flowOf(error.left()) },
                        ifRight = { list ->
                            val flows = list.take(3).map { movie ->
                                getMovieCredits(movie.tmdbId).map { creditsEither ->
                                    creditsEither
                                        .mapLeft { SuggestionError.Source(it) }
                                        .map { credits -> forYouMovieUiModelMapper.toUiModel(movie, credits) }
                                }
                            }
                            combine(flows) { eithers ->
                                eithers.filterIsInstance<Either.Right<ForYouMovieUiModel>>().map { right ->
                                    right.value
                                }.right()
                            }
                        }
                    )
                }
                .collectLatest { either ->

                    either.fold(
                        ifLeft = { error ->
                            updateState { currentState ->
                                currentState.copy(suggestedMovie = toSuggestionsState(error))
                            }
                        },
                        ifRight = { list -> suggestionsStack.join(list) }
                    )
                }
        }
    }

    override fun submit(action: ForYouAction) {
        when (action) {
            is ForYouAction.AddToWatchlist -> onAddToWatchlist(action.movieId)
            is ForYouAction.Dislike -> onDislike(action.movieId)
            is ForYouAction.Like -> onLike(action.movieId)
        }.exhaustive
    }

    private fun onAddToWatchlist(movieId: TmdbMovieId) {
        suggestionsStack.pop()
        viewModelScope.launch { addMovieToWatchlist(movieId) }
    }

    private fun onDislike(movieId: TmdbMovieId) {
        suggestionsStack.pop()
        viewModelScope.launch { addMovieToDislikedList(movieId) }
    }

    private fun onLike(movieId: TmdbMovieId) {
        suggestionsStack.pop()
        viewModelScope.launch { addMovieToLikedList(movieId) }
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
