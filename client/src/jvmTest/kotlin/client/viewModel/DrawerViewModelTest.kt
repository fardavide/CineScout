package client.viewModel

import assert4k.*
import client.util.ViewModelTest
import domain.auth.LinkToTmdb
import domain.profile.GetPersonalTmdbProfile
import entities.Test.DummyProfile
import entities.auth.TmdbAuth
import entities.right
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.test.*
import kotlin.time.minutes

class DrawerViewModelTest : ViewModelTest {

    private val getPersonalTmdbProfile = mockk<GetPersonalTmdbProfile> {
        every { this@mockk() } returns flowOf(DummyProfile)
    }
    private val linkToTest = mockk<LinkToTmdb> {
        every { this@mockk() } returns flowOf(
            LinkToTmdb.State.Login(TmdbAuth.LoginState.Loading),
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
}
