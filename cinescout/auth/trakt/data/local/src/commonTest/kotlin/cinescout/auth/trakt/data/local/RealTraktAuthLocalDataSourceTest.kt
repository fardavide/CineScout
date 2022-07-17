package cinescout.auth.trakt.data.local

import cinescout.auth.trakt.data.local.mapper.toDatabaseAccessToken
import cinescout.auth.trakt.data.local.mapper.toDatabaseRefreshToken
import cinescout.auth.trakt.data.local.mapper.toDatabaseTokens
import cinescout.auth.trakt.data.testdata.TraktAuthTestData
import cinescout.database.TraktTokensQueries
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTraktAuthLocalDataSourceTest {

    private val tokensQueries: TraktTokensQueries = mockk(relaxUnitFun = true) {
        every { find().executeAsOneOrNull() } returns TraktAuthTestData.AccessAndRefreshToken.toDatabaseTokens()
    }
    private val dataSource = RealTraktAuthLocalDataSource(tokensQueries = tokensQueries)

    @Test
    fun `find tokens from Queries`() = runTest {
        // given
        val expected = TraktAuthTestData.AccessAndRefreshToken

        // when
        val result = dataSource.findTokensBlocking()

        // then
        assertEquals(expected, result)
        verify { tokensQueries.find().executeAsOneOrNull() }
    }

    @Test
    fun `store tokens does call Queries`() = runTest {
        // given
        val tokens = TraktAuthTestData.AccessAndRefreshToken

        // when
        dataSource.storeTokens(tokens)

        // then
        coVerify {
            tokensQueries.insertTokens(
                accessToken = tokens.accessToken.toDatabaseAccessToken(),
                refreshToken = tokens.refreshToken.toDatabaseRefreshToken()
            )
        }
    }
}
