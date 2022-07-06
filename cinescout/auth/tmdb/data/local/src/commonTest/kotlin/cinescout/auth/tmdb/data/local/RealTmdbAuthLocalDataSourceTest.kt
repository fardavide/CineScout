package cinescout.auth.tmdb.data.local

import cinescout.auth.tmdb.data.local.mapper.toDatabaseAccessToken
import cinescout.auth.tmdb.data.local.mapper.toDatabaseAccountId
import cinescout.auth.tmdb.data.local.mapper.toDatabaseSessionId
import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.auth.tmdb.data.testdata.TmdbAuthTestData
import cinescout.database.TmdbCredentialsQueries
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class RealTmdbAuthLocalDataSourceTest {

    private val credentialsQueries: TmdbCredentialsQueries = mockk(relaxed = true)
    private val dataSource = RealTmdbAuthLocalDataSource(credentialsQueries = credentialsQueries)

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
