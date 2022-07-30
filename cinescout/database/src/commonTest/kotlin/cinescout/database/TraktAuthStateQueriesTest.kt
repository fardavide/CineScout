package cinescout.database

import cinescout.database.model.DatabaseTraktAuthStateValue
import cinescout.database.model.UniqueDatabaseId
import cinescout.database.testdata.DatabaseTraktTokensTestData
import cinescout.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TraktAuthStateQueriesTest : DatabaseTest() {

    private val queries = database.traktAuthStateQueries

    @Test
    fun insertAndFindAuthState() {
        // given
        val accessToken = DatabaseTraktTokensTestData.AccessToken
        val accountId = DatabaseTraktTokensTestData.AuthorizationCode
        val refreshToken = DatabaseTraktTokensTestData.RefreshToken
        val state = DatabaseTraktAuthStateValue.Completed
        val expected = TraktAuthState(
            id = UniqueDatabaseId,
            accessToken = accessToken,
            authorizationCode = accountId,
            refreshToken = refreshToken,
            state = state
        )

        // when
        queries.insertState(
            accessToken = accessToken,
            authorizationCode = accountId,
            refreshToken = refreshToken,
            state = state
        )
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun doesStoreCredentialsOnce() {
        // given
        val accessToken = DatabaseTraktTokensTestData.AccessToken
        val accountId = DatabaseTraktTokensTestData.AuthorizationCode
        val refreshToken = DatabaseTraktTokensTestData.RefreshToken
        val state = DatabaseTraktAuthStateValue.AppAuthorized
        val anotherAccessToken = DatabaseTraktTokensTestData.AnotherAccessToken
        val anotherAccountId = DatabaseTraktTokensTestData.AnotherAuthorizationCode
        val anotherRefreshToken = DatabaseTraktTokensTestData.AnotherRefreshToken
        val anotherState = DatabaseTraktAuthStateValue.Completed
        val expected = TraktAuthState(
            id = UniqueDatabaseId,
            accessToken = anotherAccessToken,
            authorizationCode = anotherAccountId,
            refreshToken = anotherRefreshToken,
            state = anotherState
        )

        // when
        queries.insertState(
            accessToken = accessToken,
            authorizationCode = accountId,
            refreshToken = refreshToken,
            state = state
        )
        queries.insertState(
            accessToken = anotherAccessToken,
            authorizationCode = anotherAccountId,
            refreshToken = anotherRefreshToken,
            state = anotherState
        )
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }
}
