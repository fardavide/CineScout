package cinescout.auth.tmdb.data.local

import cinescout.auth.tmdb.data.local.mapper.findDatabaseAccessToken
import cinescout.auth.tmdb.data.local.mapper.findDatabaseAccountId
import cinescout.auth.tmdb.data.local.mapper.findDatabaseRequestToken
import cinescout.auth.tmdb.data.local.mapper.findDatabaseSessionId
import cinescout.auth.tmdb.data.local.mapper.toDatabaseCredentials
import cinescout.auth.tmdb.data.local.mapper.toDatabaseTmdbAuthState
import cinescout.auth.tmdb.data.local.mapper.toDatabaseTmdbAuthStateValue
import cinescout.auth.tmdb.data.model.TmdbAuthState
import cinescout.auth.tmdb.data.testdata.TmdbAuthTestData
import cinescout.database.TmdbAuthStateQueries
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
    private val authStateQueries: TmdbAuthStateQueries = mockk(relaxUnitFun = true) {
        every { find().executeAsOneOrNull() } returns
            TmdbAuthState.Completed(TmdbAuthTestData.Credentials).toDatabaseTmdbAuthState()
    }
    private val dataSource = RealTmdbAuthLocalDataSource(
        authStateQueries = authStateQueries
    )

    @Test
    fun `find credentials from Queries`() = runTest {
        // given
        val expected = TmdbAuthTestData.Credentials

        // when
        val result = dataSource.findCredentialsBlocking()

        // then
        assertEquals(expected, result)
        verify { authStateQueries.find().executeAsOneOrNull() }
    }

    @Test
    fun `store request token created auth state does call Queries`() = runTest {
        // given
        val state = TmdbAuthState.RequestTokenCreated(TmdbAuthTestData.RequestToken)

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
        val state = TmdbAuthState.RequestTokenAuthorized(TmdbAuthTestData.AuthorizedRequestToken)

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
        val state = TmdbAuthState.AccessTokenCreated(TmdbAuthTestData.AccessTokenAndAccountId)

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
        val state = TmdbAuthState.Completed(TmdbAuthTestData.Credentials)

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
