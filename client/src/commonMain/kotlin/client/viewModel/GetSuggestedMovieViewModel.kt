package client.viewModel

import client.viewModel.GetSuggestedMovieViewModel.State.NoSuggestions
import client.viewModel.GetSuggestedMovieViewModel.State.Loading
import client.viewModel.GetSuggestedMovieViewModel.State.Success
import domain.stats.AddMovieToWatchlist
import domain.stats.GetSuggestedMovies
import domain.stats.RateMovie
import domain.stats.RemoveSuggestion
import entities.foldMap
import entities.model.UserRating
import entities.movies.Movie
import entities.toRight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import util.DispatchersProvider
import util.await
import kotlin.time.seconds

class GetSuggestedMovieViewModel(
    override val scope: CoroutineScope,
    getSuggestedMovies: GetSuggestedMovies,
    private val addMovieToWatchlist: AddMovieToWatchlist,
    private val rateMovie: RateMovie,
    private val removeSuggestion: RemoveSuggestion
) : CineViewModel {

    private val _result = MutableStateFlow<State>(Loading)
    val result = _result.asStateFlow()


    init {
        getSuggestedMovies()
            .toRight( { NoSuggestions }, { Success(it.first()) })
            .onEach { _result.value = it }
            .launchIn(scope)
    }

    fun skipCurrent() {
        val movie = consumeCurrentMovie() ?: return

        scope.launch {
            removeSuggestion(movie)
        }
    }

    fun likeCurrent() {
        val movie = consumeCurrentMovie() ?: return

        scope.launch {
            rateMovie(movie, UserRating.Positive)
            removeSuggestion(movie)
        }
    }

    fun dislikeCurrent() {
        val movie = consumeCurrentMovie() ?: return

        scope.launch {
            rateMovie(movie, UserRating.Negative)
            removeSuggestion(movie)
        }
    }

    fun addCurrentToWatchlist() {
        val movie = consumeCurrentMovie() ?: return

        scope.launch {
            addMovieToWatchlist(movie)
        }
    }


    private fun consumeCurrentMovie(): Movie? {
        val movie = (result.value as? Success)?.movie ?: return null
        _result.value = Loading
        return movie
    }


    sealed class State {

        object Loading : State()
        object NoSuggestions : State()
        class Success(val movie: Movie): State()
    }
}
