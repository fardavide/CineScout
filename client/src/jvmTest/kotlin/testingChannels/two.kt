package testingChannels

import client.DispatchersProvider
import client.TestDispatchersProvider
import client.ViewStateFlow
import client.viewModel.CineViewModel
import domain.Test.Movie.Inception
import domain.Test.Movie.TheBookOfEli
import entities.movies.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlin.random.Random
import kotlin.test.Test

class ExampleTwo {

    private class SUT(
        override val scope: CoroutineScope
    ): CineViewModel, DispatchersProvider by TestDispatchersProvider() {
        private val getSuggestedMovies = { listOf(Inception, TheBookOfEli) }

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

        fun next() {
            scope.launch {
                result.data = buffer.receive()
            }
        }

        override fun closeChannels() {
            buffer.cancel()
        }
    }

    // * * * TESTS * * *

    @Test
    fun `one`() = runBlockingTest {
        val sut = SUT(this)

        sut.result.take(3).collect {
            println(it)
            sut.next()
        }

        cleanupTestCoroutines()
    }

    @Test
    fun `two`() = runBlockingTest {
        val sut = SUT(this)

        sut.result.take(3).collect {
            println(it)
            sut.next()
        }

        sut.closeChannels()
        cleanupTestCoroutines()
    }

    @Test
    fun `three`() = runBlockingTest {
        val scope = TestCoroutineScope()
        val sut = SUT(scope)

        sut.result.take(3).collect {
            println(it)
            sut.next()
        }

        sut.closeChannels()
        scope.cleanupTestCoroutines()
    }

    @Test
    fun `four`() = runBlockingTest {
        val scope = TestCoroutineScope()
        val sut = SUT(scope)

        sut.result.take(3).collect {
            println(it)
            sut.next()
        }

        scope.cancel()
    }
}
