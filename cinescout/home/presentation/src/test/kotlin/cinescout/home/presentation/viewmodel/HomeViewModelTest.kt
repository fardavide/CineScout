package cinescout.home.presentation.viewmodel

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.GetAppVersion
import cinescout.account.domain.model.GetAccountError
import cinescout.account.tmdb.domain.testdata.TmdbAccountTestData
import cinescout.account.tmdb.domain.usecase.GetTmdbAccount
import cinescout.account.trakt.domain.testData.TraktAccountTestData
import cinescout.account.trakt.domain.usecase.GetTraktAccount
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.auth.tmdb.domain.usecase.NotifyTmdbAppAuthorized
import cinescout.auth.trakt.domain.testdata.TraktTestData
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.auth.trakt.domain.usecase.NotifyTraktAppAuthorized
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.TextRes
import cinescout.design.testdata.MessageTextResTestData
import cinescout.error.NetworkError
import cinescout.home.presentation.model.HomeAction
import cinescout.home.presentation.model.HomeState
import cinescout.home.presentation.testdata.HomeStateTestData
import cinescout.home.presentation.testdata.HomeStateTestData.AccountsBuilder.AccountError
import cinescout.home.presentation.testdata.HomeStateTestData.HomeStateBuilder.LoginError
import cinescout.home.presentation.testdata.HomeStateTestData.buildHomeState
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import studio.forface.cinescout.design.R.string
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HomeViewModelTest {

    private val getAppVersion: GetAppVersion = mockk {
        every { this@mockk() } returns 123
    }
    private val getTmdbAccount: GetTmdbAccount = mockk {
        every { this@mockk() } returns flowOf(GetAccountError.NoAccountConnected.left())
    }
    private val getTraktAccount: GetTraktAccount = mockk {
        every { this@mockk() } returns flowOf(GetAccountError.NoAccountConnected.left())
    }
    private val linkToTmdb: LinkToTmdb = mockk {
        every { this@mockk() } returns emptyFlow()
    }
    private val linkToTrakt: LinkToTrakt = mockk {
        every { this@mockk() } returns emptyFlow()
    }
    private val networkErrorMapper = object : NetworkErrorToMessageMapper() {
        override fun toMessage(networkError: NetworkError) = MessageTextResTestData.NoNetworkError
    }
    private val notifyTmdbAppAuthorized: NotifyTmdbAppAuthorized = mockk(relaxUnitFun = true)
    private val notifyTraktAppAuthorized: NotifyTraktAppAuthorized = mockk(relaxUnitFun = true)
    private val viewModel by lazy {
        HomeViewModel(
            getAppVersion = getAppVersion,
            getTmdbAccount = getTmdbAccount,
            getTraktAccount = getTraktAccount,
            linkToTmdb = linkToTmdb,
            linkToTrakt = linkToTrakt,
            networkErrorMapper = networkErrorMapper,
            notifyTmdbAppAuthorized = notifyTmdbAppAuthorized,
            notifyTraktAppAuthorized = notifyTraktAppAuthorized
        )
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `does call link to tmdb`() = runTest {
        // when
        viewModel.submit(HomeAction.LoginToTmdb)

        // then
        verify { linkToTmdb() }
    }

    @Test
    fun `does call link to trakt`() = runTest {
        // when
        viewModel.submit(HomeAction.LoginToTrakt)

        // then
        verify { linkToTrakt() }
    }

    @Test
    fun `does notify Tmdb app authorized`() = runTest {
        // when
        viewModel.submit(HomeAction.NotifyTmdbAppAuthorized)

        // then
        coVerify { notifyTmdbAppAuthorized() }
    }

    @Test
    fun `does notify Trakt app authorized`() = runTest {
        // given
        val authorizationCode = TraktTestData.AuthorizationCode

        // when
        viewModel.submit(HomeAction.NotifyTraktAppAuthorized(authorizationCode))

        // then
        coVerify { notifyTraktAppAuthorized(authorizationCode) }
    }

    @Test
    fun `initial state is loading`() = runTest {
        // given
        val expected = buildHomeState()

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `given logged in, when get Tmdb account, show account`() = runTest {
        // given
        val expected = buildHomeState {
            accounts {
                tmdb = HomeStateTestData.TmdbAccount
            }
        }
        every { getTmdbAccount() } returns flowOf(TmdbAccountTestData.Account.right())

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `given logged in, when get Trakt account, show account`() = runTest {
        // given
        val expected = buildHomeState {
            accounts {
                trakt = HomeStateTestData.TraktAccount
            }
        }
        every { getTraktAccount() } returns flowOf(TraktAccountTestData.Account.right())

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `given not logged in, when get Tmdb account, show error`() = runTest {
        // given
        val expected = buildHomeState {
            accounts {
                tmdb = HomeState.Accounts.Account.NoAccountConnected
            }
        }
        every { getTmdbAccount() } returns flowOf(GetAccountError.NoAccountConnected.left())

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `given not logged in, when get Trakt account, show error`() = runTest {
        // given
        val expected = buildHomeState {
            accounts {
                trakt = HomeState.Accounts.Account.NoAccountConnected
            }
        }
        every { getTraktAccount() } returns flowOf(GetAccountError.NoAccountConnected.left())

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `given logged in, when get Tmdb account, show error`() = runTest {
        // given
        val errorText = MessageTextResTestData.NoNetworkError
        val expected = buildHomeState {
            accounts {
                tmdb = errorText `as` AccountError
            }
        }
        every { getTmdbAccount() } returns flowOf(GetAccountError.Network(NetworkError.NoNetwork).left())

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `given logged in, when get Trakt account, show error`() = runTest {
        // given
        val errorText = MessageTextResTestData.NoNetworkError
        val expected = buildHomeState {
            accounts {
                trakt = errorText `as` AccountError
            }
        }
        every { getTraktAccount() } returns flowOf(GetAccountError.Network(NetworkError.NoNetwork).left())

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `show error when user did not authorize Tmdb token`() = runTest {
        // given
        val errorText = TextRes(string.home_login_app_not_authorized)
        val expected = buildHomeState {
            login = errorText `as` LoginError
        }
        every { linkToTmdb() } returns flowOf(LinkToTmdb.Error.UserDidNotAuthorizeToken.left())

        // when
        viewModel.submit(HomeAction.LoginToTmdb)
        viewModel.state.test {

            // then
            assertEquals(expected.loginEffect.consume(), awaitItem().loginEffect.consume())
        }
    }

    @Test
    fun `show error while linking to Trakt`() = runTest {
        // given
        val errorText = TextRes(string.home_login_app_not_authorized)
        val expected = buildHomeState {
            login = errorText `as` LoginError
        }
        every { linkToTrakt() } returns flowOf(LinkToTrakt.Error.UserDidNotAuthorizeApp.left())

        // when
        viewModel.submit(HomeAction.LoginToTrakt)
        viewModel.state.test {

            // then
            assertEquals(expected.loginEffect.consume(), awaitItem().loginEffect.consume())
        }
    }

    @Test
    fun `show message for network while linking to Tmdb`() = runTest {
        // given
        val errorText = MessageTextResTestData.NoNetworkError
        val expected = buildHomeState {
            login = errorText `as` LoginError
        }
        every { linkToTmdb() } returns flowOf(LinkToTmdb.Error.Network(NetworkError.NoNetwork).left())

        // when
        viewModel.submit(HomeAction.LoginToTmdb)
        viewModel.state.test {

            // then
            assertEquals(expected.loginEffect.consume(), awaitItem().loginEffect.consume())
        }
    }

    @Test
    fun `show message for network while linking to Trakt`() = runTest {
        // given
        val errorText = MessageTextResTestData.NoNetworkError
        val expected = buildHomeState {
            login = errorText `as` LoginError
        }
        every { linkToTrakt() } returns flowOf(LinkToTrakt.Error.Network(NetworkError.NoNetwork).left())

        // when
        viewModel.submit(HomeAction.LoginToTrakt)
        viewModel.state.test {

            // then
            assertEquals(expected.loginEffect.consume(), awaitItem().loginEffect.consume())
        }
    }
}
