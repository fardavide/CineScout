package cinescout.database

import cinescout.database.testdata.DatabaseGenreTestData
import cinescout.database.testutil.DatabaseTest
import org.junit.jupiter.api.Assertions.assertEquals

class GenreQueriesTest : DatabaseTest() {

    private val queries get() = database.genreQueries

    @Test
    fun insertAndFindGenre() {
        // given
        val genre = DatabaseGenreTestData.Action

        // when
        queries.insertGenre(tmdbId = genre.tmdbId, name = genre.name)
        val result = queries.findById(genre.tmdbId).executeAsOneOrNull()

        // then
        assertEquals(genre, result)
    }
}
