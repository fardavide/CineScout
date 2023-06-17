package cinescout.auth.trakt.data.local

import app.cash.sqldelight.db.QueryResult
import cinescout.auth.domain.sample.TraktAuthorizationCodeSample
import cinescout.auth.trakt.data.local.mapper.findDatabaseAccessToken
import cinescout.auth.trakt.data.local.mapper.findDatabaseAuthorizationCode
import cinescout.auth.trakt.data.local.mapper.findDatabaseRefreshToken
import cinescout.auth.trakt.data.local.mapper.toDatabaseTraktAuthState
import cinescout.auth.trakt.data.local.mapper.toDatabaseTraktAuthStateValue
import cinescout.auth.trakt.data.model.TraktAuthState
import cinescout.auth.trakt.data.sample.TraktAccessAndRefreshTokensSample
import cinescout.database.TraktAuthStateQueries
import cinescout.database.model.DatabaseTraktAuthState
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

class RealTraktAuthLocalDataSourceTest : AnnotationSpec() {

    private val authStateQueries: TraktAuthStateQueries = mockk(relaxUnitFun = true) {
        every { find().executeAsOneOrNull() } returns
            TraktAuthState.Completed(TraktAccessAndRefreshTokensSample.Tokens).toDatabaseTraktAuthState()
        coEvery { find().execute<DatabaseTraktAuthState>(any()) } returns QueryResult.Value(
            TraktAuthState.Completed(TraktAccessAndRefreshTokensSample.Tokens).toDatabaseTraktAuthState()
        )
    }
    private val dispatcher = StandardTestDispatcher()
    private val dataSource = RealTraktAuthLocalDataSource(
        authStateQueries = authStateQueries,
        dispatcher = dispatcher,
        writeDispatcher = dispatcher
    )

    @Test
    fun `find tokens from Queries`() = runTest(dispatcher) {
        // given
        val expected = TraktAccessAndRefreshTokensSample.Tokens

        // when
        val result = dataSource.findTokens()

        // then
        result shouldBe expected
        verify { authStateQueries.find().executeAsOneOrNull() }
    }

    @Test
    fun `store app authorized does call Queries`() = runTest(dispatcher) {
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
    fun `store completed does call Queries`() = runTest(dispatcher) {
        // given
        val state = TraktAuthState.Completed(TraktAccessAndRefreshTokensSample.Tokens)

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
