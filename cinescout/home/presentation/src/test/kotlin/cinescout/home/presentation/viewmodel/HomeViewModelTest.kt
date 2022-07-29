package cinescout.home.presentation.viewmodel

import app.cash.turbine.test
import arrow.core.left
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.auth.tmdb.domain.usecase.NotifyTmdbAppAuthorized
import cinescout.auth.trakt.domain.testdata.TraktTestData
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.auth.trakt.domain.usecase.NotifyTraktAppAuthorized
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.TextRes
import cinescout.error.NetworkError
import cinescout.home.presentation.model.HomeAction
import cinescout.home.presentation.model.HomeState
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import studio.forface.cinescout.design.R.string
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HomeViewModelTest {

    private val linkToTmdb: LinkToTmdb = mockk {
        every { this@mockk() } returns flowOf()
    }
    private val linkToTrakt: LinkToTrakt = mockk {
        every { this@mockk() } returns flowOf()
    }
    private val networkErrorMapper = object : NetworkErrorToMessageMapper() {
        override fun toMessage(networkError: NetworkError) = NetworkErrorTextRes
    }
    private val notifyTmdbAppAuthorized: NotifyTmdbAppAuthorized = mockk(relaxUnitFun = true)
    private val notifyTraktAppAuthorized: NotifyTraktAppAuthorized = mockk(relaxUnitFun = true)
    private val viewModel = HomeViewModel(
        linkToTmdb = linkToTmdb,
        linkToTrakt = linkToTrakt,
        networkErrorMapper = networkErrorMapper,
        notifyTmdbAppAuthorized = notifyTmdbAppAuthorized,
        notifyTraktAppAuthorized = notifyTraktAppAuthorized
    )

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
    fun `initial state is idle`() = runTest {
        // given
        val expected = HomeState.Idle

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `show error when user did not authorize Tmdb token`() = runTest {
        // given
        val expected = HomeState.Error(TextRes(string.home_login_app_not_authorized))
        every { linkToTmdb() } returns flowOf(LinkToTmdb.Error.UserDidNotAuthorizeToken.left())

        // when
        viewModel.submit(HomeAction.LoginToTmdb)
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `show error while linking to Trakt`() = runTest {
        // given
        val expected = HomeState.Error(TextRes(string.home_login_app_not_authorized))
        every { linkToTrakt() } returns flowOf(LinkToTrakt.Error.UserDidNotAuthorizeApp.left())

        // when
        viewModel.submit(HomeAction.LoginToTrakt)
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `show message for network while linking to Tmdb`() = runTest {
        // given
        val expected = HomeState.Error(NetworkErrorTextRes)
        every { linkToTmdb() } returns flowOf(LinkToTmdb.Error.Network(NetworkError.NoNetwork).left())

        // when
        viewModel.submit(HomeAction.LoginToTmdb)
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `show message for network while linking to Trakt`() = runTest {
        // given
        val expected = HomeState.Error(NetworkErrorTextRes)
        every { linkToTrakt() } returns flowOf(LinkToTrakt.Error.Network(NetworkError.NoNetwork).left())

        // when
        viewModel.submit(HomeAction.LoginToTrakt)
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    private companion object TestData {

        val NetworkErrorTextRes = TextRes("error")
    }
}
