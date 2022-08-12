package cinescout.suggestions.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.left
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.SuggestionError
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.usecase.AddMovieToDislikedList
import cinescout.movies.domain.usecase.AddMovieToLikedList
import cinescout.movies.domain.usecase.AddMovieToWatchlist
import cinescout.movies.domain.usecase.GetMovieExtras
import cinescout.suggestions.domain.usecase.GetSuggestedMovies
import cinescout.suggestions.presentation.mapper.ForYouMovieUiModelMapper
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouMovieUiModel
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.util.FixedSizeStack
import cinescout.suggestions.presentation.util.isEmpty
import cinescout.suggestions.presentation.util.joinBy
import cinescout.suggestions.presentation.util.pop
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.kotlin.exhaustive
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch

internal class ForYouViewModel(
    private val addMovieToDislikedList: AddMovieToDislikedList,
    private val addMovieToLikedList: AddMovieToLikedList,
    private val addMovieToWatchlist: AddMovieToWatchlist,
    private val forYouMovieUiModelMapper: ForYouMovieUiModelMapper,
    private val getMovieExtras: GetMovieExtras,
    private val getSuggestedMovies: GetSuggestedMovies,
    private val networkErrorMapper: NetworkErrorToMessageMapper,
    suggestionsStackSize: Int = 10
) : CineScoutViewModel<ForYouAction, ForYouState>(initialState = ForYouState.Loading) {

    private val suggestionsStack: MutableStateFlow<FixedSizeStack<ForYouMovieUiModel>> =
        MutableStateFlow(FixedSizeStack.empty(suggestionsStackSize))

    init {
        viewModelScope.launch {
            getSuggestedMovies().transformLatest { either ->
                either
                    .tapLeft { error ->
                        emit(error.left())
                    }
                    .tap { movies ->
                        ensureActive()
                        val moviesToProcess = movies.filterNot { it in suggestionsStack }
                        for (movie in moviesToProcess) {
                            val movieExtrasFlow = getMovieExtras(movie).map { either ->
                                either.mapLeft(::toSuggestionError)
                            }
                            emitAll(movieExtrasFlow)
                        }
                    }
            }.collect { moviesEither ->
                moviesEither.fold(
                    ifLeft = { error ->
                        if (suggestionsStack.isEmpty()) {
                            updateState { currentState ->
                                currentState.copy(suggestedMovie = toSuggestionsState(error))
                            }
                        }
                    },
                    ifRight = { movie ->
                        val model = forYouMovieUiModelMapper.toUiModel(movie)
                        suggestionsStack.joinBy(model) { it.tmdbMovieId }
                    }
                )
            }
        }

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

    private fun toSuggestionError(dataError: DataError): SuggestionError =
        when (dataError) {
            is DataError.Local.NoCache -> SuggestionError.NoSuggestions
            is DataError.Remote -> SuggestionError.Source(dataError)
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

internal operator fun StateFlow<FixedSizeStack<ForYouMovieUiModel>>.contains(movie: Movie) =
    movie.tmdbId in value.all().map { model -> model.tmdbMovieId }
