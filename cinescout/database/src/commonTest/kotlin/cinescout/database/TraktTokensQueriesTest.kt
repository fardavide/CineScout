package cinescout.database

import cinescout.database.testdata.DatabaseTraktTokensTestData
import cinescout.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TraktTokensQueriesTest : DatabaseTest() {

    private val queries = database.traktTokensQueries

    @Test
    fun insertAndFindTokens() {
        // given
        val accessToken = DatabaseTraktTokensTestData.AccessToken
        val refreshToken = DatabaseTraktTokensTestData.RefreshToken
        val expected = TraktTokens(id = 0, accessToken = accessToken, refreshToken = refreshToken)

        // when
        queries.insertTokens(accessToken = accessToken, refreshToken = refreshToken)
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun doesStoreTokensOnce() {
        // given
        val accessToken = DatabaseTraktTokensTestData.AccessToken
        val refreshToken = DatabaseTraktTokensTestData.RefreshToken
        val anotherAccessToken = DatabaseTraktTokensTestData.AnotherAccessToken
        val anotherRefreshToken = DatabaseTraktTokensTestData.AnotherRefreshToken
        val expected = TraktTokens(
            id = 0,
            accessToken = anotherAccessToken,
            refreshToken = anotherRefreshToken
        )

        // when
        queries.insertTokens(accessToken = accessToken, refreshToken = refreshToken)
        queries.insertTokens(
            accessToken = anotherAccessToken,
            refreshToken = anotherRefreshToken
        )
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }
}
