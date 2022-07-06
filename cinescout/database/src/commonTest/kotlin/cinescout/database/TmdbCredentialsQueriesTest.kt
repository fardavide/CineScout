package cinescout.database

import cinescout.database.testdata.DatabaseMovieTestData
import cinescout.database.testdata.DatabaseTmdbCredentialsTestData
import cinescout.database.testutil.DatabaseTest
import kotlin.test.*

class TmdbCredentialsQueriesTest : DatabaseTest() {

    private val queries = database.tmdbCredentialsQueries

    @Test
    fun `insert and find credentials`() {
        // given
        val accessToken = DatabaseTmdbCredentialsTestData.AccessToken
        val accountId = DatabaseTmdbCredentialsTestData.AccountId
        val sessionId = DatabaseTmdbCredentialsTestData.SessionId
        val expected = TmdbCredentials(id = 0, accessToken = accessToken, accountId = accountId, sessionId = sessionId)

        // when
        queries.insertCredentials(accessToken = accessToken, accountId = accountId, sessionId = sessionId)
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `does store credentials once`() {
        // given
        val accessToken = DatabaseTmdbCredentialsTestData.AccessToken
        val accountId = DatabaseTmdbCredentialsTestData.AccountId
        val sessionId = DatabaseTmdbCredentialsTestData.SessionId
        val anotherAccessToken = DatabaseTmdbCredentialsTestData.AnotherAccessToken
        val anotherAccountId = DatabaseTmdbCredentialsTestData.AnotherAccountId
        val anotherSessionId = DatabaseTmdbCredentialsTestData.AnotherSessionId
        val expected = TmdbCredentials(
            id = 0,
            accessToken = anotherAccessToken,
            accountId = anotherAccountId,
            sessionId = anotherSessionId
        )

        // when
        queries.insertCredentials(accessToken = accessToken, accountId = accountId, sessionId = sessionId)
        queries.insertCredentials(
            accessToken = anotherAccessToken,
            accountId = anotherAccountId,
            sessionId = anotherSessionId
        )
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }
}
