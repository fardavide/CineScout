package client

import assert4k.*
import domain.MockMovieRepository
import domain.SearchMovies
import domain.Test.Movie.Fury
import domain.Test.Movie.Inception
import domain.Test.Movie.SinCity
import domain.Test.Movie.TheBookOfEli
import domain.Test.Movie.TheGreatDebaters
import entities.movies.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import util.ViewStateTest
import kotlin.test.Test

internal class SearchViewModelTest: ViewStateTest() {

    private fun CoroutineScope.ViewModel(
        searchMovies: SearchMovies = SearchMovies(MockMovieRepository())
    ) = SearchViewModel(
        this,
        TestDispatchersProvider(),
        searchMovies,
    )

    @Test
    fun `Can catch exceptions and deliver with result`() {
        val exception = Exception("Something has happened")
        runBlockingTest {
            val vm = ViewModel(searchMovies = mockk {
                coEvery { this@mockk.invoke(any()) } answers {
                    throw exception
                }
            })

            vm.search("")
            assert that vm.result.next() `is` type<ViewState.Error>()

            vm.closeChannels()
        }
    }

    @Test
    fun `Returns right movies collection`() = runBlockingTest {
        val vm = ViewModel()

        vm.search("The Book")
        assert that vm.result.nextData().first() equals TheBookOfEli

        vm.search("The")
        assert that vm.result.nextData() `contains all` setOf(TheBookOfEli, TheGreatDebaters)

        vm.closeChannels()
    }

    @Test
    fun `Debounces too quick inputs`() = runBlockingTest {
        val spySearchMovies = spyk(SearchMovies(MockMovieRepository()))
        val vm = ViewModel(searchMovies = spySearchMovies)

        val result = mutableListOf<Collection<Movie>>()
        val job2 = launch {
            vm.result.onlyData().toList(result)
        }

        vm.search("Fury")
        // No wait time, data is not available yes
        advanceTimeBy(0)
        assert that vm.result.data `is` Null

        // Wait 500ms, data has been published
        advanceTimeBy(600)
        assert that vm.result.data!! `equals no order` setOf(Fury)

        vm.search("Inception")
        // Wait 500ms, data has been published
        advanceTimeBy(600)
        assert that vm.result.data!! `equals no order` setOf(Inception)

        vm.search("Pulp Fiction")
        // Short wait time, still old data
        advanceTimeBy(90)
        assert that vm.result.data!! `equals no order` setOf(Inception)

        vm.search("Sin City")
        // Wait 500ms, data has been published
        advanceTimeBy(600)
        assert that vm.result.data!! `equals no order` setOf(SinCity)

        coVerify(exactly = 3) { spySearchMovies(any()) }
        assert that result.size equals 3

        job2.cancel()
        vm.closeChannels()
    }
}
