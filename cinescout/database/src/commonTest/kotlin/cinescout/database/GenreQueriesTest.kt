package cinescout.database

import cinescout.database.sample.DatabaseGenreSample
import cinescout.database.testutil.DatabaseTest
import org.junit.jupiter.api.Assertions.assertEquals

class GenreQueriesTest : DatabaseTest() {

    private val queries get() = database.genreQueries

    @Test
    fun insertAndFindGenre() {
        // given
        val genre = DatabaseGenreSample.Action

        // when
        queries.insertGenre(slug = genre.slug, name = genre.name)
        val result = queries.findById(genre.slug).executeAsOneOrNull()

        // then
        assertEquals(genre, result)
    }
}
