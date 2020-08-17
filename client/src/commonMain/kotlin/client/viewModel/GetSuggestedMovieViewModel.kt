package client.viewModel

import client.DispatchersProvider
import client.ViewStateFlow
import domain.GetSuggestedMovies
import domain.RateMovie
import entities.movies.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class GetSuggestedMovieViewModel(
    override val scope: CoroutineScope,
    dispatchers: DispatchersProvider,
    private val getSuggestedMovies: GetSuggestedMovies,
    private val rateMovie: RateMovie
) : CineViewModel, DispatchersProvider by dispatchers {

    val result = ViewStateFlow<Movie>()

    private val buffer = Channel<Movie>(5)

    init {
        next()

        scope.launch(Io) {
            while(isActive) {
                try {
                    // Take 5 random from suggestions
                    val suggestions = getSuggestedMovies()
                        .shuffled(Random)
                        .take(5)

                    // Send to buffer if not full, otherwise wait
                    for (suggestion in suggestions)
                        buffer.send(suggestion)

                } catch (t: Throwable) {
                    result.error = t
                    delay(500)
                }
            }
        }
    }

    fun skip() {

    }

    fun like() {

    }

    fun dislike() {

    }

    private fun next() {
        scope.launch {
            result.data = buffer.receive()
        }
    }

    override fun closeChannels() {
        buffer.cancel()
    }
}
