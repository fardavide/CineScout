package client.viewModel

import assert4k.*
import client.util.ViewModelTest
import domain.auth.IsTmdbLoggedIn
import domain.auth.IsTraktLoggedIn
import domain.auth.Link
import domain.auth.LinkToTmdb
import domain.auth.LinkToTrakt
import domain.profile.GetPersonalTmdbProfile
import entities.Either
import entities.TestData.DummyProfile
import entities.auth.Auth.LoginState
import entities.right
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlin.test.*
import kotlin.time.minutes

class DrawerViewModelTest : ViewModelTest {

    private val hasLoginCompleted = MutableStateFlow(false)
    private val getPersonalTmdbProfile = mockk<GetPersonalTmdbProfile> {
        every { this@mockk() } returns flowOf(DummyProfile.right())
    }
    private val isTmdbLoggedIn = mockk<IsTmdbLoggedIn> {
        every { this@mockk() } returns hasLoginCompleted
    }
    private val isTraktLoggedIn = mockk<IsTraktLoggedIn> {
        every { this@mockk() } returns hasLoginCompleted
    }
    private val noCodeApproveRequestToken = LoginState.ApproveRequestToken.WithoutCode("", Channel())
    private val codeApproveRequestToken = LoginState.ApproveRequestToken.WithCode("", Channel())
    private val linkToTmdb = mockk<LinkToTmdb> {
        every { this@mockk() } returns flowOf(
            Link.State.Login(LoginState.Loading),
            Link.State.Login(noCodeApproveRequestToken),
            Link.State.Login(LoginState.Completed),
        ).onCompletion { hasLoginCompleted.value = true }.map { it.right() }
    }
    private val linkToTrakt = mockk<LinkToTrakt> {
        every { this@mockk() } returns flowOf(
            Link.State.Login(LoginState.Loading),
            Link.State.Login(codeApproveRequestToken),
            Link.State.Login(LoginState.Completed),
        ).onCompletion { hasLoginCompleted.value = true }.map { it.right() }
    }

    @Test
    fun `profile is emitted correctly after Tmdb login`() = viewModelTest(
        ::DrawerViewModel,
        ignoreUnfinishedJobs = true
    ) { viewModel ->

        val result = mutableListOf<Any>(viewModel.profile.value)
        advanceTimeBy(5.minutes.toLongMilliseconds())
        result += "input"
        viewModel.startLinkingToTmdb()
        result += viewModel.profile.value

        assert that result equals listOf(
            DrawerViewModel.ProfileState.LoggedOut,
            "input",
            DrawerViewModel.ProfileState.LoggedIn(DummyProfile),
        )
    }

    @Test
    fun `profile is emitted correctly after Trakt login`() = viewModelTest(
        ::DrawerViewModel,
        ignoreUnfinishedJobs = true
    ) { viewModel ->

        val result = mutableListOf<Any>(viewModel.profile.value)
        advanceTimeBy(5.minutes.toLongMilliseconds())
        result += "input"
        viewModel.startLinkingToTrakt()
        result += viewModel.profile.value

        assert that result equals listOf(
            DrawerViewModel.ProfileState.LoggedOut,
            "input",
            DrawerViewModel.ProfileState.LoggedIn(DummyProfile),
        )
    }

    @Test
    fun `profile is emitted correctly is the user is already logged in to Tmdb`() = viewModelTest(
        {
            every { isTmdbLoggedIn() } returns flowOf(true)
            DrawerViewModel(this)
        },
        ignoreUnfinishedJobs = true
    ) { viewModel ->
        val result = viewModel.profile.first()
        assert that result equals DrawerViewModel.ProfileState.LoggedIn(DummyProfile)
    }

    @Test
    fun `profile is emitted correctly is the user is already logged in to Trakt`() = viewModelTest(
        {
            every { isTraktLoggedIn() } returns flowOf(true)
            DrawerViewModel(this)
        },
        ignoreUnfinishedJobs = true
    ) { viewModel ->
        val result = viewModel.profile.first()
        assert that result equals DrawerViewModel.ProfileState.LoggedIn(DummyProfile)
    }

    @Test
    fun `token approval request is prompted correctly for Tmdb`() = viewModelTest(
        ::DrawerViewModel,
        ignoreUnfinishedJobs = true
    ) { viewModel ->

        val result = mutableListOf<Either<Link.Error, Link.State>>()
        launch {
            viewModel.tmdbLinkResult.toList(result)
        }
        viewModel.startLinkingToTmdb()

        val state = result[result.lastIndex - 1].rightOrNull()
        assert that state `is` type<Link.State.Login>()
        val loginState = (state as Link.State.Login).loginState
        assert that loginState `is` type<LoginState.ApproveRequestToken.WithoutCode>()
    }

    @Test
    fun `token approval request is prompted correctly for Trakt`() = viewModelTest(
        ::DrawerViewModel,
        ignoreUnfinishedJobs = true
    ) { viewModel ->

        val result = mutableListOf<Either<Link.Error, Link.State>>()
        launch {
            viewModel.tmdbLinkResult.toList(result)
        }
        viewModel.startLinkingToTrakt()

        val state = result[result.lastIndex - 1].rightOrNull()
        assert that state `is` type<Link.State.Login>()
        val loginState = (state as Link.State.Login).loginState
        assert that loginState `is` type<LoginState.ApproveRequestToken.WithCode>()
    }

    private fun DrawerViewModel(scope: CoroutineScope) =
        DrawerViewModel(scope, getPersonalTmdbProfile, isTmdbLoggedIn, isTraktLoggedIn, linkToTmdb, linkToTrakt)
}
