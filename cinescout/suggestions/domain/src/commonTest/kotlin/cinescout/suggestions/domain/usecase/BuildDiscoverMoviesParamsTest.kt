package cinescout.suggestions.domain.usecase

import arrow.core.nonEmptyListOf
import cinescout.movies.domain.testdata.DiscoverMoviesParamsTestData
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BuildDiscoverMoviesParamsTest {

    private val buildDiscoverMoviesParams = BuildDiscoverMoviesParams(shouldIncludeAllTheParam = true)

    @Test
    fun `when success`() = runTest {
        // given
        val expected = DiscoverMoviesParamsTestData.FromInception
        val movie = MovieWithExtrasTestData.Inception.copy(
            credits = MovieWithExtrasTestData.Inception.credits.copy(
                cast = nonEmptyListOf(MovieWithExtrasTestData.Inception.credits.cast.first()),
                crew = nonEmptyListOf(MovieWithExtrasTestData.Inception.credits.crew.first())
            ),
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
