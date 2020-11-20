package client.viewModel

import assert4k.*
import client.util.ViewModelTest
import domain.MockStatRepository
import domain.Test.Movie.AmericanGangster
import domain.Test.Movie.TheBookOfEli
import domain.stats.GetMoviesInWatchlist
import entities.right
import entities.stats.StatRepository
import kotlinx.coroutines.CoroutineScope
import kotlin.test.*

class WatchlistViewModelTest : ViewModelTest {

    private val repository: StatRepository = MockStatRepository()
    private fun CoroutineScope.ViewModel() =
        WatchlistViewModel(this, GetMoviesInWatchlist(repository))

    @Test
    fun `returns right watchlist`() = viewModelTest(ignoreUnfinishedJobs = true, buildViewModel = {

        repository.addToWatchlist(TheBookOfEli)
        repository.addToWatchlist(AmericanGangster)
        ViewModel()

    }) { viewModel ->

        assert that viewModel.result.value equals GetMoviesInWatchlist.State.Success(listOf(
            TheBookOfEli,
            AmericanGangster
        )).right()
    }

    @Test
    fun `returns NoMovies is watchlist is empty`() = viewModelTest(ignoreUnfinishedJobs = true, buildViewModel = {
        ViewModel()
    }) { viewModel ->

        assert that viewModel.result.value.leftOrNull() `is` type<GetMoviesInWatchlist.Error.NoMovies>()
    }
}
