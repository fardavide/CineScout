package client.viewModel

import assert4k.*
import client.util.ViewModelTest
import domain.auth.LinkToTmdb
import domain.profile.GetPersonalTmdbProfile
import entities.Either
import entities.Test.DummyProfile
import entities.auth.TmdbAuth
import entities.right
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlin.test.*
import kotlin.time.minutes

class DrawerViewModelTest : ViewModelTest {

    private val getPersonalTmdbProfile = mockk<GetPersonalTmdbProfile> {
        every { this@mockk() } returns flowOf(DummyProfile)
    }
    private val approveRequestToken = TmdbAuth.LoginState.ApproveRequestToken("", Channel())
    private val linkToTest = mockk<LinkToTmdb> {
        every { this@mockk() } returns flowOf(
            LinkToTmdb.State.Login(TmdbAuth.LoginState.Loading),
            LinkToTmdb.State.Login(approveRequestToken),
            LinkToTmdb.State.Login(TmdbAuth.LoginState.Completed),
        ).map { it.right() }
    }

    @Test
    fun `profile is emitted correctly after the login`() = viewModelTest(
        { DrawerViewModel(this, getPersonalTmdbProfile, linkToTest) },
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
            DrawerViewModel.ProfileState.LoggedIn(DummyProfile)
        )
    }

    @Test
    fun `token approval request is prompted correctly`() = viewModelTest(
        { DrawerViewModel(this, getPersonalTmdbProfile, linkToTest) },
        ignoreUnfinishedJobs = true
    ) { viewModel ->

        val result = mutableListOf<Either<LinkToTmdb.Error, LinkToTmdb.State>>()
        launch {
            viewModel.tmdbLinkResult.toList(result)
        }
        viewModel.startLinkingToTmdb()

        val state = result[result.lastIndex - 1].rightOrNull()
        assert that state `is` type<LinkToTmdb.State.Login>()
        val loginState = (state as LinkToTmdb.State.Login).loginState
        assert that loginState `is` type<TmdbAuth.LoginState.ApproveRequestToken>()
    }
}
