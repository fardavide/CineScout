package client.viewModel

import assert4k.*
import client.util.ViewStateTest
import client.viewModel.SearchViewModel.Companion.QueryDebounceInterval
import domain.MockMovieRepository
import domain.SearchMovies
import domain.Test.Movie.Fury
import domain.Test.Movie.Inception
import domain.Test.Movie.SinCity
import domain.Test.Movie.TheBookOfEli
import domain.Test.Movie.TheGreatDebaters
import entities.NetworkError
import entities.filterRight
import entities.firstRight
import entities.left
import entities.movies.Movie
import entities.movies.SearchError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.DelayController
import util.test.advanceTimeBy
import kotlin.test.*
import kotlin.time.seconds

internal class SearchViewModelTest : ViewStateTest {

    private fun CoroutineScope.ViewModel(
        searchMovies: SearchMovies = SearchMovies(MockMovieRepository()),
    ): SearchViewModel {
        return SearchViewModel(
            this,
            searchMovies,
        )
    }

    @Test
    fun `Can catch exceptions and deliver with result`() = coroutinesTest {
        val vm = ViewModel(searchMovies = mockk {
            coEvery { this@mockk(any()) } returns SearchError.Network(NetworkError.NoNetwork).left()
        })

        vm search "no"
        skipDebounce()
        assert that vm.result.first() equals SearchError.Network(NetworkError.NoNetwork).left()

        vm.closeChannels()
        cleanupTestCoroutines()
    }

    @Test
    fun `Returns right movies collection`() = coroutinesTest {
        val vm = ViewModel()

        vm search "The Book"
        skipDebounce()
        assert that vm.result.firstRight().first() equals TheBookOfEli

        vm search "The"
        skipDebounce()
        assert that vm.result.firstRight() `contains all` setOf(TheBookOfEli, TheGreatDebaters)

        vm.closeChannels()
    }

    @Test
    fun `Debounces too quick inputs`() = coroutinesTest {
        val spySearchMovies = spyk(SearchMovies(MockMovieRepository()))
        val vm = ViewModel(searchMovies = spySearchMovies)

        val result = mutableListOf<Collection<Movie>>()
        val job2 = launch {
            vm.result.filterRight().toList(result)
        }

        vm search "Fury"
        // No wait time, data is not available yes
        advanceTimeBy(0)
        assert that vm.result.value equals SearchError.EmptyQuery.left()

        // Wait debounce, data has been published
        skipDebounce()
        assert that vm.result.firstRight() `equals no order` setOf(Fury)

        vm search "Inception"
        // Wait debounce, data has been published
        skipDebounce()
        assert that vm.result.firstRight() `equals no order` setOf(Inception)

        vm search "Pulp Fiction"
        // Short wait time, still old data
        advanceTimeBy(90)
        assert that vm.result.firstRight() `equals no order` setOf(Inception)

        vm search "Sin City"
        // Wait debounce, data has been published
        skipDebounce()
        assert that vm.result.firstRight() `equals no order` setOf(SinCity)

        coVerify(exactly = 3) { spySearchMovies(any()) }
        assert that result.size equals 3

        job2.cancel()
        vm.closeChannels()
    }

    private fun DelayController.skipDebounce() =
        advanceTimeBy(QueryDebounceInterval + 1.seconds)
}
