package client.viewModel

import client.ViewState
import client.ViewState.Loading
import client.ViewStateFlow
import domain.AddMovieToWatchlist
import domain.GetSuggestedMovies
import domain.RateMovie
import entities.Rating
import entities.movies.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import util.DispatchersProvider
import util.await
import kotlin.time.seconds

class GetSuggestedMovieViewModel(
    override val scope: CoroutineScope,
    dispatchers: DispatchersProvider,
    private val getSuggestedMovies: GetSuggestedMovies,
    private val addMovieToWatchlist: AddMovieToWatchlist,
    private val rateMovie: RateMovie
) : CineViewModel, DispatchersProvider by dispatchers {

    val result = ViewStateFlow<Movie, Error>()
    private val stack = mutableListOf<Movie>()
    private val rated = mutableListOf<Movie>()

    init {
        loadIfNeededAndPublishWhenReady()
    }

    fun skipCurrent() {
        loadIfNeededAndPublishWhenReady()
    }

    fun likeCurrent() {
        scope.launch(Io) {
            result.data?.let { movie ->
                rated += movie
                rateMovie(movie, Rating.Positive)
            }
        }
        loadIfNeededAndPublishWhenReady()
    }

    fun dislikeCurrent() {
        scope.launch(Io) {
            result.data?.let { movie ->
                rated += movie
                rateMovie(movie, Rating.Negative)
            }
        }
        loadIfNeededAndPublishWhenReady()
    }

    fun addCurrentToWatchlist() {
        scope.launch(Io) {
            result.data?.let { movie ->
                rated += movie
                addMovieToWatchlist(movie)
            }
            loadIfNeededAndPublishWhenReady()
        }
    }

    private fun loadIfNeededAndPublishWhenReady() {
        result.state = Loading
        scope.launch(Io) {
            loadIfNeeded()
        }
        val publishJob = scope.launch(Io) {
            await { stack.isNotEmpty() }
            var next: Movie
            do {
                next = stack.removeFirst()
            } while (next in rated)
            result.data = next
        }
        scope.launch {
            delay(30.seconds)
            publishJob.cancel()
        }
    }

    private suspend fun loadIfNeeded() {
        var errorCount = 0
        var iterationCount = 0
        while (errorCount < 3 && iterationCount < 10 && stack.size < BUFFER_SIZE) {
            try {
                stack += (getSuggestedMovies((errorCount + 5) * ++iterationCount) - stack)

            } catch (t: NoSuchElementException) {
                errorCount++
                if (result.state is Loading)
                result set Error.NoRatedMovies

            } catch (t: Throwable) {
                errorCount++
                if (result.state is Loading)
                result set Error.Unknown(t)
            }
            yield()
        }
    }

    private fun loadForeverWhenNeeded() {
        scope.launch(Io) {
            while (true) {
                await { stack.size < BUFFER_SIZE }
                loadIfNeeded()
                yield()
            }
        }
    }

    sealed class Error(override val throwable: Throwable? = null) : ViewState.Error() {

        object NoRatedMovies : GetSuggestedMovieViewModel.Error()
        class Unknown(override val throwable: Throwable) : GetSuggestedMovieViewModel.Error()
    }

    companion object {
        const val BUFFER_SIZE = 10
    }
}
