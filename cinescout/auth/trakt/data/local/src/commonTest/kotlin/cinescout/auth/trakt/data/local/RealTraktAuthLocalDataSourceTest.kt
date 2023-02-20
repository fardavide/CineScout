package cinescout.auth.trakt.data.local

import app.cash.sqldelight.db.QueryResult
import cinescout.auth.trakt.data.local.mapper.findDatabaseAccessToken
import cinescout.auth.trakt.data.local.mapper.findDatabaseAuthorizationCode
import cinescout.auth.trakt.data.local.mapper.findDatabaseRefreshToken
import cinescout.auth.trakt.data.local.mapper.toDatabaseTraktAuthState
import cinescout.auth.trakt.data.local.mapper.toDatabaseTraktAuthStateValue
import cinescout.auth.trakt.data.model.TraktAuthState
import cinescout.auth.trakt.data.sample.TraktAccessAndRefreshTokensSample
import cinescout.auth.trakt.domain.sample.TraktAuthorizationCodeSample
import cinescout.database.TraktAuthStateQueries
import cinescout.database.model.DatabaseTraktAuthState
import io.mockk.coEvery
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
            TraktAuthState.Completed(TraktAccessAndRefreshTokensSample.AccessAndRefreshToken).toDatabaseTraktAuthState()
        coEvery { find().execute<DatabaseTraktAuthState>(any()) } returns QueryResult.Value(
            TraktAuthState.Completed(TraktAccessAndRefreshTokensSample.AccessAndRefreshToken).toDatabaseTraktAuthState()
        )
    }
    private val dispatcher = StandardTestDispatcher()
    private val dataSource = RealTraktAuthLocalDataSource(
        authStateQueries = authStateQueries,
        dispatcher = dispatcher
    )

    @Test
    fun `find auth state from Queries`() = runTest(dispatcher) {
        // given
        val expected = TraktAuthState.Completed(TraktAccessAndRefreshTokensSample.AccessAndRefreshToken)

        // when
        val result = dataSource.findAuthState().first()

        // then
        assertEquals(expected, result)
        coVerify { authStateQueries.find().execute<DatabaseTraktAuthState>(any()) }
    }

    @Test
    fun `find tokens from Queries`() = runTest(dispatcher) {
        // given
        val expected = TraktAccessAndRefreshTokensSample.AccessAndRefreshToken

        // when
        val result = dataSource.findTokens()

        // then
        assertEquals(expected, result)
        verify { authStateQueries.find().executeAsOneOrNull() }
    }

    @Test
    fun `store app authorized does call Queries`() = runTest {
        // given
        val state = TraktAuthState.AppAuthorized(TraktAuthorizationCodeSample.AuthorizationCode)

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
        val state = TraktAuthState.Completed(TraktAccessAndRefreshTokensSample.AccessAndRefreshToken)

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
