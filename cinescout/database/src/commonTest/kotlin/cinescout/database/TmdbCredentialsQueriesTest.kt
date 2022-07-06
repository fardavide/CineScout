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
        val sessionId = DatabaseTmdbCredentialsTestData.SessionId
        val expected = TmdbCredentials(0, accessToken, sessionId)

        // when
        queries.insertCredentials(accessToken = accessToken, sessionId = sessionId)
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `does store credentials once`() {
        // given
        val accessToken = DatabaseTmdbCredentialsTestData.AccessToken
        val sessionId = DatabaseTmdbCredentialsTestData.SessionId
        val anotherAccessToken = DatabaseTmdbCredentialsTestData.AnotherAccessToken
        val anotherSessionId = DatabaseTmdbCredentialsTestData.AnotherSessionId
        val expected = TmdbCredentials(0, anotherAccessToken, anotherSessionId)

        // when
        queries.insertCredentials(accessToken = accessToken, sessionId = sessionId)
        queries.insertCredentials(accessToken = anotherAccessToken, sessionId = anotherSessionId)
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }
}
