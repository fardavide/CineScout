package client.viewModel

import client.DispatchersProvider
import client.ViewState
import client.ViewStateFlow
import domain.GetSuggestedMovies
import domain.RateMovie
import entities.Rating
import entities.movies.Movie
import entities.util.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.seconds

class GetSuggestedMovieViewModel(
    override val scope: CoroutineScope,
    dispatchers: DispatchersProvider,
    private val getSuggestedMovies: GetSuggestedMovies,
    private val rateMovie: RateMovie
) : CineViewModel, DispatchersProvider by dispatchers {

    val result = ViewStateFlow<Movie>()
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

    private fun loadIfNeededAndPublishWhenReady() {
        result.state = ViewState.Loading
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
        while (errorCount < 3 && stack.size < BUFFER_SIZE) {
            try {
                stack += getSuggestedMovies((errorCount + 5) * ++iterationCount)

            } catch (t: Throwable) {
                errorCount++
                result.error = t
            }
        }
    }

    private fun loadForeverWhenNeeded() {
        scope.launch(Io) {
            while (true) {
                await { stack.size < BUFFER_SIZE }
                loadIfNeeded()
            }
        }
    }

    companion object {
        const val BUFFER_SIZE = 10
    }
}
