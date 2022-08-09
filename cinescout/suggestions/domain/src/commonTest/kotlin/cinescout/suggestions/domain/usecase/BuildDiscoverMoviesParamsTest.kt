package cinescout.suggestions.domain.usecase

import arrow.core.nonEmptyListOf
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.ReleaseYear
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BuildDiscoverMoviesParamsTest {

    private val buildDiscoverMoviesParams = BuildDiscoverMoviesParams()

    @Test
    fun `when success`() = runTest {
        // given
        val expected = DiscoverMoviesParams(
            genre = MovieWithExtrasTestData.Inception.movieWithDetails.genres.first(),
            releaseYear = ReleaseYear(MovieTestData.Inception.releaseDate.year)
        )
        val movie = MovieWithExtrasTestData.Inception.copy(
            movieWithDetails = MovieWithExtrasTestData.Inception.movieWithDetails.copy(
                genres = nonEmptyListOf(MovieWithExtrasTestData.Inception.movieWithDetails.genres.first())
            )
        )
        val movies = nonEmptyListOf(movie)

        // when
        val result = buildDiscoverMoviesParams(movies)

        // the
        assertEquals(expected, result)
    }
}
