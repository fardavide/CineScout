package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.sample.MovieCreditsSample
import cinescout.movies.domain.store.FakeMovieCreditsStore
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMovieCreditsTest {

    private val movieCreditsStore = FakeMovieCreditsStore(listOf(MovieCreditsSample.Inception))
    private val getMovieCredits = GetMovieCredits(movieCreditsStore)

    @Test
    fun `get credits from repository`() = runTest {
        // given
        val credits = MovieCreditsSample.Inception
        val expected = credits.right()

        // when
        getMovieCredits(credits.movieId).test {

            // then
            assertEquals(expected, awaitItem().dataOrNull())
            awaitComplete()
        }
    }
}
