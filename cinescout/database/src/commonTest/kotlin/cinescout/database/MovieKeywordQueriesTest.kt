package cinescout.database

import cinescout.database.testdata.DatabaseMovieKeywordTestData
import cinescout.database.testutil.DatabaseTest
import org.junit.jupiter.api.Assertions.assertEquals

class MovieKeywordQueriesTest : DatabaseTest() {

    private val queries get() = database.movieKeywordQueries

    @Test
    fun insertAndFindKeyword() {
        // given
        val movieKeyword = DatabaseMovieKeywordTestData.Corruption

        // when
        queries.insertKeyword(movieId = movieKeyword.movieId, keywordId = movieKeyword.keywordId)
        val result = queries.findAllByMovieId(movieKeyword.movieId).executeAsList()

        // then
        assertEquals(listOf(movieKeyword), result)
    }
}
