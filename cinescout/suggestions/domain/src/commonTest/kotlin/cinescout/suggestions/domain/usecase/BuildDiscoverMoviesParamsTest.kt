package cinescout.suggestions.domain.usecase

import arrow.core.left
import arrow.core.right
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.ReleaseYear
import cinescout.movies.domain.model.SuggestionError
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithRatingTestData
import cinescout.movies.domain.usecase.GetAllRatedMovies
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BuildDiscoverMoviesParamsTest {

    private val getAllRatedMovies: GetAllRatedMovies = mockk()
    private val buildDiscoverMoviesParams = BuildDiscoverMoviesParams()

    @Test
    fun `when no rated movies`() = runTest {
        // given
        val expected = SuggestionError.NoSuggestions.left()
        val movies = emptyList<MovieWithPersonalRating>()

        // when
        val result = buildDiscoverMoviesParams(movies)

        // the
        assertEquals(expected, result)
    }

    @Test
    fun `when no positively movies`() = runTest {
        // given
        val expected = SuggestionError.NoSuggestions.left()
        val movies = listOf(MovieWithRatingTestData.War)

        // when
        val result = buildDiscoverMoviesParams(movies)

        // the
        assertEquals(expected, result)
    }

    @Test
    fun `when success`() = runTest {
        // given
        val expected = DiscoverMoviesParams(ReleaseYear(MovieTestData.Inception.releaseDate.year)).right()
        val movies = listOf(MovieWithRatingTestData.Inception)

        // when
        val result = buildDiscoverMoviesParams(movies)

        // the
        assertEquals(expected, result)
    }
}
