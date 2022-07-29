package cinescout.auth.trakt.data.local

import cinescout.auth.trakt.data.local.mapper.findDatabaseAccessToken
import cinescout.auth.trakt.data.local.mapper.findDatabaseAuthorizationCode
import cinescout.auth.trakt.data.local.mapper.findDatabaseRefreshToken
import cinescout.auth.trakt.data.local.mapper.toDatabaseTraktAuthState
import cinescout.auth.trakt.data.local.mapper.toDatabaseTraktAuthStateValue
import cinescout.auth.trakt.data.model.TraktAuthState
import cinescout.auth.trakt.data.testdata.TraktAuthTestData
import cinescout.database.TraktAuthStateQueries
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTraktAuthLocalDataSourceTest {

    private val authStateQueries: TraktAuthStateQueries = mockk(relaxUnitFun = true) {
        every { find().executeAsOneOrNull() } returns
            TraktAuthState.Completed(TraktAuthTestData.AccessAndRefreshToken).toDatabaseTraktAuthState()
    }
    private val dispatcher = StandardTestDispatcher()
    private val dataSource = RealTraktAuthLocalDataSource(
        authStateQueries = authStateQueries,
        dispatcher = dispatcher
    )

    @Test
    fun `find auth state from Queries`() = runTest(dispatcher) {
        // given
        val expected = TraktAuthState.Completed(TraktAuthTestData.AccessAndRefreshToken)

        // when
        val result = dataSource.findAuthState().first()

        // then
        assertEquals(expected, result)
        verify { authStateQueries.find().executeAsOneOrNull() }
    }

    @Test
    fun `find tokens from Queries`() = runTest {
        // given
        val expected = TraktAuthTestData.AccessAndRefreshToken

        // when
        val result = dataSource.findTokensBlocking()

        // then
        assertEquals(expected, result)
        verify { authStateQueries.find().executeAsOneOrNull() }
    }

    @Test
    fun `store app authorized does call Queries`() = runTest {
        // given
        val state = TraktAuthState.AppAuthorized(TraktAuthTestData.AuthorizationCode)

        // when
        dataSource.storeAuthState(state)

        // then
        coVerify {
            authStateQueries.insertState(
                state = state.toDatabaseTraktAuthStateValue(),
                accessToken = state.findDatabaseAccessToken(),
                authorizationCode = state.findDatabaseAuthorizationCode(),
                refreshToken = state.findDatabaseRefreshToken()
            )
        }
    }

    @Test
    fun `store completed does call Queries`() = runTest {
        // given
        val state = TraktAuthState.Completed(TraktAuthTestData.AccessAndRefreshToken)

        // when
        dataSource.storeAuthState(state)

        // then
        coVerify {
            authStateQueries.insertState(
                state = state.toDatabaseTraktAuthStateValue(),
                accessToken = state.findDatabaseAccessToken(),
                authorizationCode = state.findDatabaseAuthorizationCode(),
                refreshToken = state.findDatabaseRefreshToken()
            )
        }
    }
}
