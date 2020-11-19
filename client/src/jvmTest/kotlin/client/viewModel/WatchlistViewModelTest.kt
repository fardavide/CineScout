package client.viewModel

import assert4k.*
import client.util.ViewModelTest
import domain.MockStatRepository
import domain.Test.Movie.AmericanGangster
import domain.Test.Movie.TheBookOfEli
import domain.stats.GetMoviesInWatchlist
import entities.stats.StatRepository
import kotlinx.coroutines.CoroutineScope
import kotlin.test.*

class WatchlistViewModelTest : ViewModelTest {

    private val repository: StatRepository = MockStatRepository()
    private fun CoroutineScope.ViewModel() =
        WatchlistViewModel(this, GetMoviesInWatchlist(repository))

    @Test
    fun `returns right watchlist`() = viewModelTest({

        repository.addToWatchlist(TheBookOfEli)
        repository.addToWatchlist(AmericanGangster)
        ViewModel()

    }) { viewModel ->

        assert that viewModel.result.data equals listOf(TheBookOfEli, AmericanGangster)
    }

    @Test
    fun `returns NoMovies is watchlist is empty`() = viewModelTest({
        ViewModel()
    }) { viewModel ->

        assert that viewModel.result.value `is` type<WatchlistViewModel.Error.NoMovies>()
    }
}
