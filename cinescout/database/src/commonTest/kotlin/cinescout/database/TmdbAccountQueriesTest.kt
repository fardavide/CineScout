package cinescout.database

import cinescout.database.model.UniqueDatabaseId
import cinescout.database.testdata.DatabaseTmdbAccountTestData
import cinescout.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TmdbAccountQueriesTest : DatabaseTest() {

    private val queries = database.tmdbAccountQueries

    @Test
    fun insertAndFindAccount() {
        // given
        val username = DatabaseTmdbAccountTestData.Username
        val expected = TmdbAccount(
            id = UniqueDatabaseId,
            username = username
        )

        // when
        queries.insertAccount(username = username)
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun doesStoreAccountOnce() {
        // given
        val username = DatabaseTmdbAccountTestData.Username
        val anotherUsername = DatabaseTmdbAccountTestData.AnotherUsername
        val expected = TmdbAccount(
            id = UniqueDatabaseId,
            username = anotherUsername
        )

        // when
        queries.insertAccount(username = username)
        queries.insertAccount(username = anotherUsername)
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }
}
