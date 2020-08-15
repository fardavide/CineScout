package client

import assert4k.*
import domain.MockMovieRepository
import domain.SearchMovies
import domain.Test.Movie.TheBookOfEli
import domain.Test.Movie.TheGreatDebaters
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test


internal class SearchViewModelTest {

    private fun ViewModel(
        searchMovies: SearchMovies = SearchMovies(MockMovieRepository())
    ) = SearchViewModel(
        TestCoroutineScope(),
        TestDispatchersProvider(),
        searchMovies,
    )

    @Test
    fun `Can catch exceptions and deliver with result`() = runBlockingTest {
        val vm = ViewModel(searchMovies = mockk {
            coEvery { this@mockk.invoke(any()) } answers {
                throw Exception("Something from happened")
            }
        })

        vm.search("")
        assert that (vm.result.value is ViewState.Error)
    }

    @Test
    fun `Returns right movies collection`() = runBlockingTest {
        val vm = ViewModel()

        vm.search("The Book")
        assert that vm.result.data?.first() equals TheBookOfEli

        vm.search("The")
        assert that vm.result.data!! `contains all` setOf(TheBookOfEli, TheGreatDebaters)
    }

    @Test
    fun `Debounces too quick inputs`() = runBlockingTest {
        val vm = ViewModel(searchMovies = mockk {
            coEvery { this@mockk.invoke(any()) } answers {
                setOf(mockk())
            }
        })
        var results = 0
        val collect = launch {
            vm.result.onlyData().collect { results++ }
        }

        vm.search("")
        assert that results equals 1

        advanceTimeBy(500)

        vm.search("")
        vm.search("")
        vm.search("")
        assert that results equals 2

        advanceTimeBy(500)

        vm.search("")
        vm.search("")
        assert that results equals 3

        collect.cancel()
    }
}
