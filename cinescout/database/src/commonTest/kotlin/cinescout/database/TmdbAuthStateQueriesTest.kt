package cinescout.database

import cinescout.database.model.DatabaseTmdbAccessToken
import cinescout.database.model.DatabaseTmdbAuthStateValue
import cinescout.database.model.DatabaseTmdbRequestToken
import cinescout.database.testdata.DatabaseTmdbCredentialsTestData
import cinescout.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TmdbAuthStateQueriesTest : DatabaseTest() {

    private val queries = database.tmdbAuthStateQueries

    @Test
    fun insertAndFindAuthState() {
        // given
        val accessToken = DatabaseTmdbCredentialsTestData.AccessToken
        val accountId = DatabaseTmdbCredentialsTestData.AccountId
        val sessionId = DatabaseTmdbCredentialsTestData.SessionId
        val requestToken = DatabaseTmdbCredentialsTestData.RequestToken
        val state = DatabaseTmdbAuthStateValue.Completed
        val expected = TmdbAuthState(
            id = 0,
            accessToken = accessToken,
            accountId = accountId,
            requestToken = requestToken,
            sessionId = sessionId,
            state = state
        )

        // when
        queries.insertState(
            accessToken = accessToken,
            accountId = accountId,
            requestToken = requestToken,
            sessionId = sessionId,
            state = state
        )
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun doesStoreCredentialsOnce() {
        // given
        val accessToken: DatabaseTmdbAccessToken? = null
        val accountId = DatabaseTmdbCredentialsTestData.AccountId
        val requestToken = DatabaseTmdbCredentialsTestData.RequestToken
        val sessionId = DatabaseTmdbCredentialsTestData.SessionId
        val state = DatabaseTmdbAuthStateValue.RequestTokenAuthorized
        val anotherAccessToken = DatabaseTmdbCredentialsTestData.AnotherAccessToken
        val anotherAccountId = DatabaseTmdbCredentialsTestData.AnotherAccountId
        val anotherRequestToken: DatabaseTmdbRequestToken? = null
        val anotherSessionId = DatabaseTmdbCredentialsTestData.AnotherSessionId
        val anotherState = DatabaseTmdbAuthStateValue.AccessTokenCreated
        val expected = TmdbAuthState(
            id = 0,
            accessToken = anotherAccessToken,
            accountId = anotherAccountId,
            requestToken = anotherRequestToken,
            sessionId = anotherSessionId,
            state = anotherState
        )

        // when
        queries.insertState(
            accessToken = accessToken,
            accountId = accountId,
            requestToken = requestToken,
            sessionId = sessionId,
            state = state
        )
        queries.insertState(
            accessToken = anotherAccessToken,
            accountId = anotherAccountId,
            requestToken = anotherRequestToken,
            sessionId = anotherSessionId,
            state = anotherState
        )
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }
}
