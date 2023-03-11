package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.sample.MovieKeywordsSample
import cinescout.movies.domain.store.FakeMovieKeywordsStore
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMovieKeywordsTest {

    private val movieKeywordsStore = FakeMovieKeywordsStore(listOf(MovieKeywordsSample.Inception))
    private val getMovieKeywords = GetMovieKeywords(movieKeywordsStore = movieKeywordsStore)

    @Test
    fun `get credits from repository`() = runTest {
        // given
        val keywords = MovieKeywordsSample.Inception
        val expected = keywords.right()

        // when
        getMovieKeywords(keywords.movieId).test {

            // then
            assertEquals(expected, awaitItem().dataOrNull())
            awaitComplete()
        }
    }
}
