package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import cinescout.movies.domain.usecase.GetMovieExtras
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetSuggestedMoviesWithExtrasTest {

    private val getSuggestedMovies: GetSuggestedMovies = mockk {
        val movies = nonEmptyListOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet,
            MovieTestData.War
        )
        every { this@mockk() } returns flowOf(movies.right())
    }
    private val getMovieExtras: GetMovieExtras = mockk {
        every { this@mockk(movie = MovieTestData.Inception, refresh = any()) } returns flow {
            delay(100)
            emit(MovieWithExtrasTestData.Inception.right())
        }
        every { this@mockk(movie = MovieTestData.TheWolfOfWallStreet, refresh = any()) } returns flow {
            delay(200)
            emit(MovieWithExtrasTestData.TheWolfOfWallStreet.right())
        }
        every { this@mockk(movie = MovieTestData.War, refresh = any()) } returns flow {
            delay(300)
            emit(MovieWithExtrasTestData.War.right())
        }
    }
    private val getSuggestedMoviesWithExtras = GetSuggestedMoviesWithExtras(
        getSuggestedMovies = getSuggestedMovies,
        getMovieExtras = getMovieExtras
    )

    @Test
    fun `progressively load extras`() = runTest {
        // given
        val expected = listOf(
            nonEmptyListOf(
                MovieWithExtrasTestData.Inception
            ),
            nonEmptyListOf(
                MovieWithExtrasTestData.Inception,
                MovieWithExtrasTestData.TheWolfOfWallStreet
            ),
            nonEmptyListOf(
                MovieWithExtrasTestData.Inception,
                MovieWithExtrasTestData.TheWolfOfWallStreet,
                MovieWithExtrasTestData.War
            )
        )

        // when
        getSuggestedMoviesWithExtras().test {

            // then
            assertEquals(expected[0].right(), awaitItem())
            assertEquals(expected[1].right(), awaitItem())
            assertEquals(expected[2].right(), awaitItem())
        }
    }
}
