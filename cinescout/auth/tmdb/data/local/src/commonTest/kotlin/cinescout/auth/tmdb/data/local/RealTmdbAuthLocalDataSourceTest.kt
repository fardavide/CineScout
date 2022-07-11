package cinescout.auth.tmdb.data.local

import cinescout.auth.tmdb.data.local.mapper.toDatabaseAccessToken
import cinescout.auth.tmdb.data.local.mapper.toDatabaseAccountId
import cinescout.auth.tmdb.data.local.mapper.toDatabaseCredentials
import cinescout.auth.tmdb.data.local.mapper.toDatabaseSessionId
import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.auth.tmdb.data.testdata.TmdbAuthTestData
import cinescout.database.TmdbCredentialsQueries
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTmdbAuthLocalDataSourceTest {

    private val credentialsQueries: TmdbCredentialsQueries = mockk(relaxUnitFun = true) {
        every { find().executeAsOneOrNull() } returns TmdbAuthTestData.Credentials.toDatabaseCredentials()
    }
    private val dataSource = RealTmdbAuthLocalDataSource(credentialsQueries = credentialsQueries)

    @Test
    fun `find credentials from Queries`() = runTest {
        // given
        val expected = TmdbAuthTestData.Credentials

        // when
        val result = dataSource.findCredentialsBlocking()

        // then
        assertEquals(expected, result)
        verify { credentialsQueries.find().executeAsOneOrNull() }
    }

    @Test
    fun `store credentials does call Queries`() = runTest {
        // given
        val credentials = TmdbAuthTestData.Credentials

        // when
        dataSource.storeCredentials(credentials)

        // then
        coVerify {
            credentialsQueries.insertCredentials(
                accessToken = credentials.accessToken.toDatabaseAccessToken(),
                accountId = credentials.accountId.toDatabaseAccountId(),
                sessionId = credentials.sessionId.toDatabaseSessionId()
            )
        }
    }
}
