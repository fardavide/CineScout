package cinescout.movies.data.local.mapper

import arrow.core.left
import arrow.core.right
import cinescout.database.model.DatabaseMovie
import cinescout.error.DataError
import cinescout.movies.domain.testdata.MovieTestData
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DatabaseMovieMapperTest {

    private val mapper = DatabaseMovieMapper()

    @Test
    fun `maps single movie`() {
        // given
        val movie = MovieTestData.Inception
        val expected = movie.right()
        val databaseMovie = DatabaseMovie(
            title = movie.title,
            tmdbId = movie.tmdbId.toDatabaseId()
        )

        // when
        val result = mapper.toMovie(databaseMovie)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `maps null movie`() {
        // given
        val expected = DataError.Local.NoCache.left()

        // when
        val result = mapper.toMovie(null)

        // then
        assertEquals(expected, result)
    }
}
