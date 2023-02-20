package cinescout.auth.tmdb.data.local

import app.cash.sqldelight.db.QueryResult
import cinescout.auth.tmdb.data.local.mapper.findDatabaseAccessToken
import cinescout.auth.tmdb.data.local.mapper.findDatabaseAccountId
import cinescout.auth.tmdb.data.local.mapper.findDatabaseRequestToken
import cinescout.auth.tmdb.data.local.mapper.findDatabaseSessionId
import cinescout.auth.tmdb.data.local.mapper.toDatabaseTmdbAuthState
import cinescout.auth.tmdb.data.local.mapper.toDatabaseTmdbAuthStateValue
import cinescout.auth.tmdb.data.model.TmdbAuthState
import cinescout.auth.tmdb.data.sample.TmdbAccessTokenAndAccountIdSample
import cinescout.auth.tmdb.data.sample.TmdbAuthorizedRequestTokenSample
import cinescout.auth.tmdb.data.sample.TmdbCredentialsSample
import cinescout.auth.tmdb.data.sample.TmdbRequestTokenSample
import cinescout.database.TmdbAuthStateQueries
import cinescout.database.model.DatabaseTmdbAuthState
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

class RealTmdbAuthLocalDataSourceTest {

    private val authStateQueries: TmdbAuthStateQueries = mockk(relaxUnitFun = true) {
        every { find().executeAsOneOrNull() } returns
            TmdbAuthState.Completed(TmdbCredentialsSample.Credentials).toDatabaseTmdbAuthState()
        coEvery { find().execute<DatabaseTmdbAuthState>(any()) } returns QueryResult.Value(
            TmdbAuthState.Completed(TmdbCredentialsSample.Credentials).toDatabaseTmdbAuthState()
        )
    }
    private val dispatcher = StandardTestDispatcher()
    private val dataSource = RealTmdbAuthLocalDataSource(
        authStateQueries = authStateQueries,
        dispatcher = dispatcher,
        writeDispatcher = dispatcher
    )

    @Test
    fun `find auth state from Queries`() = runTest(dispatcher) {
        // given
        val expected = TmdbAuthState.Completed(TmdbCredentialsSample.Credentials)

        // when
        val result = dataSource.findAuthState().first()

        // then
        assertEquals(expected, result)
        coVerify { authStateQueries.find().execute<DatabaseTmdbAuthState>(any()) }
    }

    @Test
    fun `find credentials from Queries`() = runTest(dispatcher) {
        // given
        val expected = TmdbCredentialsSample.Credentials

        // when
        val result = dataSource.findCredentials()

        // then
        assertEquals(expected, result)
        verify { authStateQueries.find().executeAsOneOrNull() }
    }

    @Test
    fun `store request token created auth state does call Queries`() = runTest {
        // given
        val state = TmdbAuthState.RequestTokenCreated(TmdbRequestTokenSample.RequestToken)

        // when
        dataSource.storeAuthState(state)

        // then
        coVerify {
            authStateQueries.insertState(
                state = state.toDatabaseTmdbAuthStateValue(),
                accessToken = state.findDatabaseAccessToken(),
                accountId = state.findDatabaseAccountId(),
                requestToken = state.findDatabaseRequestToken(),
                sessionId = state.findDatabaseSessionId()
            )
        }
    }

    @Test
    fun `store request token authorized auth state does call Queries`() = runTest {
        // given
        val state = TmdbAuthState.RequestTokenAuthorized(TmdbAuthorizedRequestTokenSample.AuthorizedRequestToken)

        // when
        dataSource.storeAuthState(state)

        // then
        coVerify {
            authStateQueries.insertState(
                state = state.toDatabaseTmdbAuthStateValue(),
                accessToken = state.findDatabaseAccessToken(),
                accountId = state.findDatabaseAccountId(),
                requestToken = state.findDatabaseRequestToken(),
                sessionId = state.findDatabaseSessionId()
            )
        }
    }

    @Test
    fun `store access token created auth state does call Queries`() = runTest {
        // given
        val state = TmdbAuthState.AccessTokenCreated(TmdbAccessTokenAndAccountIdSample.AccessTokenAndAccountId)

        // when
        dataSource.storeAuthState(state)

        // then
        coVerify {
            authStateQueries.insertState(
                state = state.toDatabaseTmdbAuthStateValue(),
                accessToken = state.findDatabaseAccessToken(),
                accountId = state.findDatabaseAccountId(),
                requestToken = state.findDatabaseRequestToken(),
                sessionId = state.findDatabaseSessionId()
            )
        }
    }

    @Test
    fun `store completed auth state does call Queries`() = runTest {
        // given
        val state = TmdbAuthState.Completed(TmdbCredentialsSample.Credentials)

        // when
        dataSource.storeAuthState(state)

        // then
        coVerify {
            authStateQueries.insertState(
                state = state.toDatabaseTmdbAuthStateValue(),
                accessToken = state.findDatabaseAccessToken(),
                accountId = state.findDatabaseAccountId(),
                requestToken = state.findDatabaseRequestToken(),
                sessionId = state.findDatabaseSessionId()
            )
        }
    }
}
