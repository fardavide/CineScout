package cinescout.database

import cinescout.database.testdata.DatabaseKeywordTestData
import cinescout.database.testutil.DatabaseTest
import org.junit.jupiter.api.Assertions.assertEquals

class KeywordQueriesTest : DatabaseTest() {

    private val queries get() = database.keywordQueries

    @Test
    fun insertAndFindGenre() {
        // given
        val keyword = DatabaseKeywordTestData.Corruption

        // when
        queries.insertKeyword(tmdbId = keyword.tmdbId, name = keyword.name)
        val result = queries.findById(keyword.tmdbId).executeAsOneOrNull()

        // then
        assertEquals(keyword, result)
    }
}
