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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
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

    val result: StateFlow<State> = getSuggestedMovies()
        .toRight( { NoSuggestions }, { Success(it.first()) })
        .stateIn(scope = scope, started = SharingStarted.Eagerly, initialValue = Loading)

    private val currentMovie: Movie? =
        (result.value as? Success)?.movie


    fun skipCurrent() {
        currentMovie ?: return

        scope.launch {
            removeSuggestion(currentMovie)
        }
    }

    fun likeCurrent() {
        currentMovie ?: return

        scope.launch {
            rateMovie(currentMovie, UserRating.Positive)
            removeSuggestion(currentMovie)
        }
    }

    fun dislikeCurrent() {
        currentMovie ?: return

        scope.launch {
            rateMovie(currentMovie, UserRating.Negative)
            removeSuggestion(currentMovie)
        }
    }

    fun addCurrentToWatchlist() {
        currentMovie ?: return

        scope.launch {
            addMovieToWatchlist(currentMovie)
        }
    }

    sealed class State {

        object Loading : State()
        object NoSuggestions : State()
        class Success(val movie: Movie): State()
    }
}
