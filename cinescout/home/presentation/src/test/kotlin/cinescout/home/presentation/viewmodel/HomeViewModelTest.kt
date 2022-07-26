package cinescout.home.presentation.viewmodel

import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.home.presentation.model.HomeAction
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test

class HomeViewModelTest {

    private val linkToTmdb: LinkToTmdb = mockk {
        every { this@mockk() } returns flowOf()
    }
    private val linkToTrakt: LinkToTrakt = mockk {
        every { this@mockk() } returns flowOf()
    }
    private val viewModel = HomeViewModel(linkToTmdb, linkToTrakt)

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
}
