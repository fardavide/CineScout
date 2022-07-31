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
        val gravatarHash = DatabaseTmdbAccountTestData.GravatarHash
        val username = DatabaseTmdbAccountTestData.Username
        val expected = TmdbAccount(
            id = UniqueDatabaseId,
            gravatarHash = gravatarHash,
            username = username
        )

        // when
        queries.insertAccount(gravatarHash = gravatarHash, username = username)
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun doesStoreAccountOnce() {
        // given
        val gravatarHash = DatabaseTmdbAccountTestData.GravatarHash
        val username = DatabaseTmdbAccountTestData.Username
        val anotherGravatarHash = DatabaseTmdbAccountTestData.AnotherGravatarHash
        val anotherUsername = DatabaseTmdbAccountTestData.AnotherUsername
        val expected = TmdbAccount(
            id = UniqueDatabaseId,
            gravatarHash = anotherGravatarHash,
            username = anotherUsername
        )

        // when
        queries.insertAccount(gravatarHash = gravatarHash, username = username)
        queries.insertAccount(gravatarHash = anotherGravatarHash, username = anotherUsername)
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }
}
