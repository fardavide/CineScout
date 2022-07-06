package cinescout.auth.tmdb.data.local

import cinescout.auth.tmdb.data.local.mapper.toDatabaseAccessToken
import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.database.TmdbCredentialsQueries
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class RealTmdbAuthLocalDataSourceTest {

    private val credentialsQueries: TmdbCredentialsQueries = mockk(relaxed = true)
    private val dataSource = RealTmdbAuthLocalDataSource(credentialsQueries = credentialsQueries)

    @Test
    fun `does call Queries`() = runTest {
        // given
        val accessToken = TmdbAccessToken("token")

        // when
        dataSource.storeAccessToken(accessToken)

        // then
        coVerify { credentialsQueries.insertCredentials(accessToken.toDatabaseAccessToken()) }
    }
}
