package client.viewModel

import client.DispatchersProvider
import client.ViewStateFlow
import client.util.takeOrFill
import domain.GetSuggestedMovies
import domain.RateMovie
import entities.Rating
import entities.movies.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GetSuggestedMovieViewModel(
    override val scope: CoroutineScope,
    dispatchers: DispatchersProvider,
    private val getSuggestedMovies: GetSuggestedMovies,
    private val rateMovie: RateMovie
) : CineViewModel, DispatchersProvider by dispatchers {

    val result = ViewStateFlow<Movie>()

    private val buffer = Channel<Movie?>(BUFFER_SIZE)

    init {
        next()
    }

    fun skipCurrent() {
        next()
    }

    fun likeCurrent() {
        scope.launch(Io) {
            rateMovie(result.data!!, Rating.Positive)
            next()
        }
    }

    fun dislikeCurrent() {
        scope.launch(Io) {
            rateMovie(result.data!!, Rating.Negative)
            next()
        }
    }

    private fun next() {
        scope.launch(Io) {
            var next: Movie? = null
            while (next == null) next = buffer.receive()
            result.data = next
        }

        scope.launch(Io) {
            preLoad()
        }
    }

    private suspend fun preLoad() {
        while(true) {
            try {
                // Take 5 random from suggestions
                val suggestions = getSuggestedMovies()
                    .shuffled(Random)
                    .takeOrFill(BUFFER_SIZE + 1)

                // Send to buffer if not full, otherwise quit
                for (suggestion in suggestions)
                    if (!buffer.offer(suggestion)) return

            } catch (t: Throwable) {
                result.error = t
                delay(500)
            }
        }
    }


    override fun closeChannels() {
        buffer.cancel()
    }

    companion object {
        const val BUFFER_SIZE = 10
    }
}
