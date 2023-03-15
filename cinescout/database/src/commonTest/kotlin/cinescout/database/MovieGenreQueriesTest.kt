package cinescout.database

import cinescout.database.sample.DatabaseScreenplayGenreSample
import cinescout.database.testutil.DatabaseTest
import org.junit.jupiter.api.Assertions.assertEquals

class MovieGenreQueriesTest : DatabaseTest() {

    private val queries get() = database.movieGenreQueries

    @Test
    fun insertAndFindGenre() {
        // given
        val movieGenre = DatabaseScreenplayGenreSample.Action

        // when
        queries.insertGenre(movieId = movieGenre.movieId, genreId = movieGenre.genreId)
        val result = queries.findAllByMovieId(movieGenre.movieId).executeAsList()

        // then
        assertEquals(listOf(movieGenre), result)
    }
}
