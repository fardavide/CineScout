package cinescout.home.presentation.viewmodel

import app.cash.turbine.ReceiveTurbine
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
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.design.testdata.MessageSample
import cinescout.error.NetworkError
import cinescout.home.presentation.model.HomeAction
import cinescout.home.presentation.model.HomeState
import cinescout.home.presentation.testdata.HomeStateTestData
import cinescout.home.presentation.testdata.HomeStateTestData.AccountsBuilder.AccountError
import cinescout.home.presentation.testdata.HomeStateTestData.HomeStateBuilder.LoginError
import cinescout.home.presentation.testdata.HomeStateTestData.buildHomeState
import cinescout.network.model.ConnectionStatus
import cinescout.network.usecase.ObserveConnectionStatus
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.StartUpdateSuggestions
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
        every { this@mockk(refresh = any()) } returns flowOf(GetAccountError.NoAccountConnected.left())
    }
    private val getTraktAccount: GetTraktAccount = mockk {
        every { this@mockk(refresh = any()) } returns flowOf(GetAccountError.NoAccountConnected.left())
    }
    private val linkToTmdb: LinkToTmdb = mockk {
        every { this@mockk() } returns emptyFlow()
    }
    private val linkToTrakt: LinkToTrakt = mockk {
        every { this@mockk() } returns emptyFlow()
    }
    private val networkErrorMapper = object : NetworkErrorToMessageMapper() {
        override fun toMessage(networkError: NetworkError) = MessageSample.NoNetworkError
    }
    private val notifyTmdbAppAuthorized: NotifyTmdbAppAuthorized = mockk(relaxUnitFun = true)
    private val notifyTraktAppAuthorized: NotifyTraktAppAuthorized = mockk(relaxUnitFun = true)
    private val observeConnectionStatus: ObserveConnectionStatus = mockk {
        every { this@mockk() } returns flowOf(ConnectionStatus.AllOnline)
    }
    private val startUpdateSuggestions: StartUpdateSuggestions = mockk(relaxUnitFun = true)
    private val viewModel by lazy {
        HomeViewModel(
            getAppVersion = getAppVersion,
            getTmdbAccount = getTmdbAccount,
            getTraktAccount = getTraktAccount,
            linkToTmdb = linkToTmdb,
            linkToTrakt = linkToTrakt,
            networkErrorMapper = networkErrorMapper,
            notifyTmdbAppAuthorized = notifyTmdbAppAuthorized,
            notifyTraktAppAuthorized = notifyTraktAppAuthorized,
            observeConnectionStatus = observeConnectionStatus,
            startUpdateSuggestions = startUpdateSuggestions
        )
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `does call link to tmdb`() = runTest {
        // when
        viewModel.submit(HomeAction.LoginToTmdb)
        advanceUntilIdle()

        // then
        verify { linkToTmdb() }
    }

    @Test
    fun `does call link to trakt`() = runTest {
        // when
        viewModel.submit(HomeAction.LoginToTrakt)
        advanceUntilIdle()

        // then
        verify { linkToTrakt() }
    }

    @Test
    fun `does notify Tmdb app authorized`() = runTest {
        // when
        viewModel.submit(HomeAction.NotifyTmdbAppAuthorized)
        advanceUntilIdle()

        // then
        coVerify { notifyTmdbAppAuthorized() }
    }

    @Test
    fun `does notify Trakt app authorized`() = runTest {
        // given
        val authorizationCode = TraktTestData.AuthorizationCode

        // when
        viewModel.submit(HomeAction.NotifyTraktAppAuthorized(authorizationCode))
        advanceUntilIdle()

        // then
        coVerify { notifyTraktAppAuthorized(authorizationCode) }
    }

    @Test
    fun `update suggested movies after login to Tmdb`() = runTest {
        // given
        every { linkToTmdb() } returns flowOf(LinkToTmdb.State.Success.right())

        // when
        viewModel.submit(HomeAction.LoginToTmdb)
        advanceUntilIdle()

        // then
        coVerify { startUpdateSuggestions(suggestionsMode = SuggestionsMode.Quick) }
    }

    @Test
    fun `update suggested movies after login to Trakt`() = runTest {
        // given
        every { linkToTrakt() } returns flowOf(LinkToTrakt.State.Success.right())

        // when
        viewModel.submit(HomeAction.LoginToTrakt)
        advanceUntilIdle()

        // then
        coVerify { startUpdateSuggestions(suggestionsMode = SuggestionsMode.Quick) }
    }

    @Test
    fun `initial state is loading`() = runTest {
        // given
        val expected = HomeState.Loading

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
        every { getTmdbAccount(refresh = any()) } returns flowOf(TmdbAccountTestData.Account.right())

        // when
        viewModel.state.test {
            awaitLoading()

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
        every { getTraktAccount(refresh = any()) } returns flowOf(TraktAccountTestData.Account.right())

        // when
        viewModel.state.test {
            awaitLoading()

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
        every { getTmdbAccount(refresh = any()) } returns flowOf(GetAccountError.NoAccountConnected.left())

        // when
        viewModel.state.test {
            awaitLoading()

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
        every { getTraktAccount(refresh = any()) } returns flowOf(GetAccountError.NoAccountConnected.left())

        // when
        viewModel.state.test {
            awaitLoading()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `given logged in, when get Tmdb account, show error`() = runTest {
        // given
        val errorText = MessageSample.NoNetworkError
        val expected = buildHomeState {
            accounts {
                tmdb = errorText `as` AccountError
            }
        }
        every { getTmdbAccount(refresh = any()) } returns flowOf(GetAccountError.Network(NetworkError.NoNetwork).left())

        // when
        viewModel.state.test {
            awaitLoading()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `given logged in, when get Trakt account, show error`() = runTest {
        // given
        val errorText = MessageSample.NoNetworkError
        val expected = buildHomeState {
            accounts {
                trakt = errorText `as` AccountError
            }
        }
        every { getTraktAccount(refresh = any()) } returns
            flowOf(GetAccountError.Network(NetworkError.NoNetwork).left())

        // when
        viewModel.state.test {
            awaitLoading()

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
            awaitLoading()

            // then
            assertEquals(expected.loginEffect.consume(), awaitItem().loginEffect.consume())
            cancelAndIgnoreRemainingEvents()
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
            awaitLoading()

            // then
            assertEquals(expected.loginEffect.consume(), awaitItem().loginEffect.consume())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `show message for network while linking to Tmdb`() = runTest {
        // given
        val errorText = MessageSample.NoNetworkError
        val expected = buildHomeState {
            login = errorText `as` LoginError
        }
        every { linkToTmdb() } returns flowOf(LinkToTmdb.Error.Network(NetworkError.NoNetwork).left())

        // when
        viewModel.submit(HomeAction.LoginToTmdb)
        viewModel.state.test {
            awaitLoading()

            // then
            assertEquals(expected.loginEffect.consume(), awaitItem().loginEffect.consume())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `show message for network while linking to Trakt`() = runTest {
        // given
        val errorText = MessageSample.NoNetworkError
        val expected = buildHomeState {
            login = errorText `as` LoginError
        }
        every { linkToTrakt() } returns flowOf(LinkToTrakt.Error.Network(NetworkError.NoNetwork).left())

        // when
        viewModel.submit(HomeAction.LoginToTrakt)
        viewModel.state.test {
            awaitLoading()

            // then
            assertEquals(expected.loginEffect.consume(), awaitItem().loginEffect.consume())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when network status change, status is emitted`() = runTest {
        // given
        val expected = buildHomeState {
            networkStatus = ConnectionStatusUiModel.DeviceOffline
        }
        every { observeConnectionStatus() } returns flowOf(ConnectionStatus.AllOffline)

        // when
        viewModel.also { advanceUntilIdle() }.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    private suspend fun ReceiveTurbine<HomeState>.awaitLoading() {
        assertEquals(HomeState.Loading, awaitItem())
        assertEquals(HomeState.Loading.copy(appVersion = HomeState.AppVersion.Data(123)), awaitItem())
    }
}
