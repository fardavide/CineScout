package cinescout.database

import cinescout.database.model.UniqueDatabaseId
import cinescout.database.testdata.DatabaseTraktAccountTestData
import cinescout.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TraktAccountQueriesTest : DatabaseTest() {

    private val queries = database.traktAccountQueries

    @Test
    fun insertAndFindAccount() {
        // given
        val gravatarHash = DatabaseTraktAccountTestData.GravatarHash
        val username = DatabaseTraktAccountTestData.Username
        val expected = TraktAccount(
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
        val gravatarHash = DatabaseTraktAccountTestData.GravatarHash
        val username = DatabaseTraktAccountTestData.Username
        val anotherGravatarHash = DatabaseTraktAccountTestData.AnotherGravatarHash
        val anotherUsername = DatabaseTraktAccountTestData.AnotherUsername
        val expected = TraktAccount(
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
