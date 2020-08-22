package client.viewModel

import client.DispatchersProvider
import client.ViewStateFlow
import domain.GetSuggestedMovies
import domain.RateMovie
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

    init {
        loadIfNeededAndPublishWhenReady()
    }

    fun skipCurrent() {
        loadIfNeededAndPublishWhenReady()
    }

    fun likeCurrent() {
    }

    fun dislikeCurrent() {
    }

    private fun loadIfNeededAndPublishWhenReady() {
        scope.launch(Io) {
            loadIfNeeded()
        }
        val j = scope.launch(Io) {
            await(30.seconds) { stack.isNotEmpty() }
            result.data = stack.removeFirst()
        }
        // TODO needed because 'Test finished with active jobs'
        //  bug?
        scope.launch {
            delay(30.seconds)
            j.cancel()
        }
    }

    private suspend fun loadIfNeeded() {
        var errorCount = 0
        while (errorCount < 3 && stack.size < BUFFER_SIZE) {
            try {
                stack += getSuggestedMovies()

            } catch (t: Throwable) {
                errorCount++
                result.error = t
            }
        }
    }

    companion object {
        const val BUFFER_SIZE = 10
    }
}
