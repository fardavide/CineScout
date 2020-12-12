package client.viewModel

import assert4k.*
import client.util.ViewModelTest
import client.viewModel.GetSuggestedMovieViewModel.State.NoSuggestions
import client.viewModel.GetSuggestedMovieViewModel.State.Success
import domain.DiscoverMovies
import domain.MockMovieRepository
import domain.MockStatRepository
import domain.Test.Movie.AmericanGangster
import domain.Test.Movie.DejaVu
import domain.Test.Movie.Inception
import domain.Test.Movie.TheBookOfEli
import domain.Test.Movie.TheGreatDebaters
import domain.stats.AddMovieToWatchlist
import domain.stats.GenerateDiscoverParams
import domain.stats.GenerateMoviesSuggestions
import domain.stats.GetSuggestedMovies
import domain.stats.GetSuggestionData
import domain.stats.RateMovie
import domain.stats.RemoveSuggestion
import entities.MissingCache
import entities.ResourceError
import entities.left
import entities.model.UserRating
import entities.movies.Movie
import entities.right
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import util.test.second
import kotlin.test.*

internal class GetSuggestedMovieViewModelTest : ViewModelTest {

    private val stats = MockStatRepository()
    private val rateMovie = RateMovie(stats)
    private val mockAddMovieToWatchlist = mockk<AddMovieToWatchlist>(relaxed = true)

    private fun CoroutineScope.ViewModel(
        randomize: Boolean = false,
        getSuggestedMovies: GetSuggestedMovies = GetSuggestedMovies(
            stats = stats,
            generateMoviesSuggestions = GenerateMoviesSuggestions(
                discover = DiscoverMovies(MockMovieRepository()),
                generateDiscoverParams = GenerateDiscoverParams(randomize = randomize),
                getSuggestionsData = GetSuggestionData(stats),
                stats = stats,
                logger = Logger()
            )
        ),
        addMovieToWatchlist: AddMovieToWatchlist = AddMovieToWatchlist(stats),
    ) = GetSuggestedMovieViewModel(
        this,
        getSuggestedMovies,
        addMovieToWatchlist,
        rateMovie,
        removeSuggestion = RemoveSuggestion(stats)
    )

    @Test
    @Ignore("Inspect why failing")
    fun `deliver NoSuggestions if no suggestion is stored`() = viewModelTest(
        ignoreUnfinishedJobs = true,
        buildViewModel = { ViewModel() }
    ) { viewModel ->

        assert that viewModel.result.value `is` type<NoSuggestions>()
    }

    @Test
    fun `can deliver suggested movie`() = viewModelTest(
        ignoreUnfinishedJobs = true,
        buildViewModel = {

            // Add some like movies for give more data to the test
            rateMovie(Inception, UserRating.Positive)
            rateMovie(TheBookOfEli, UserRating.Positive)
            rateMovie(TheGreatDebaters, UserRating.Positive)

            ViewModel()

        }
    ) { viewModel ->

        assert that viewModel.result.value `is` type<Success>()
    }

    @Test
    fun `can add movie to watchlist`() = viewModelTest(
        ignoreUnfinishedJobs = true,
        buildViewModel = {

            // Add some like movies for give more data to the test
            rateMovie(DejaVu, UserRating.Positive)
            rateMovie(Inception, UserRating.Positive)
            rateMovie(TheGreatDebaters, UserRating.Positive)

            ViewModel(addMovieToWatchlist = mockAddMovieToWatchlist)
        }
    ) { viewModel ->

        val movie = (viewModel.result.value as Success).movie
        viewModel.addCurrentToWatchlist()

        coVerify { mockAddMovieToWatchlist(movie) }
    }

    @Test
    fun `Skip shown next movie`() = viewModelTest(
        ignoreUnfinishedJobs = true,
        buildViewModel = {

            // Add some like movies for give more data to the test
            rateMovie(DejaVu, UserRating.Positive)
            rateMovie(Inception, UserRating.Positive)
            rateMovie(TheGreatDebaters, UserRating.Positive)

            ViewModel()

        }
    ) { viewModel ->

        val first = viewModel.result.value
        assert that first `is` type<Success>()

        viewModel.skipCurrent()
        val second = viewModel.result.value
        assert that second * {
            it `is not` Null
            it `not equals` first
        }
    }

    @Test
    fun `Does not stop delivering suggestions`() = viewModelTest(
        ignoreUnfinishedJobs = true,
        buildViewModel = {

            // Add some like movies for give more data to the test
            rateMovie(Inception, UserRating.Positive)
            rateMovie(TheBookOfEli, UserRating.Positive)
            rateMovie(TheGreatDebaters, UserRating.Positive)

            ViewModel()

        }
    ) { viewModel ->

        var last: MovieCount? = null

        repeat(100) { count ->
            val current = viewModel.result.value
            assert that current * {
                it `is not` Null { "Error on iteration #$count" }
                +(this + count) `not equals` last
            }
            last = current + count
            viewModel.skipCurrent()
        }
    }

    @Test
    fun `Does not show liked movies`() = viewModelTest(
        ignoreUnfinishedJobs = true,
        buildViewModel = {

            // Add some like movies for give more data to the test
            rateMovie(TheBookOfEli, UserRating.Positive)
            rateMovie(TheGreatDebaters, UserRating.Positive)

            ViewModel()

        }
    ) { viewModel ->

        val current = viewModel.result.value
        assert that current `is` type<Success>()

        viewModel.likeCurrent()

        repeat(100) { count ->
            assert that viewModel.result.value * {
                it `is` type<Success>()() { "Error on iteration #$count" }
                it `not equals` current { "Error on iteration #$count" }
            }
            viewModel.skipCurrent()
        }
    }

    @Test
    fun `does not show disliked movies`() = viewModelTest(
        ignoreUnfinishedJobs = true,
        buildViewModel = {

            // Add some like movies for give more data to the test
            rateMovie(TheBookOfEli, UserRating.Positive)
            rateMovie(TheGreatDebaters, UserRating.Positive)

            ViewModel()

        }
    ) { viewModel ->

        val current = viewModel.result.value
        assert that current `is` type<Success>()

        viewModel.dislikeCurrent()

        repeat(100) { count ->
            assert that viewModel.result.value * {
                it `is not` Null { "Error on iteration #$count" }
                it `not equals` current { "Error on iteration #$count" }
            }
            viewModel.skipCurrent()
        }
    }

    @Test
    fun `can recover after error`() = viewModelTest(
        ignoreUnfinishedJobs = true,
        buildViewModel = {

            ViewModel(getSuggestedMovies = mockk {
                coEvery { this@mockk() } returns flowOf(
                    ResourceError.Local(MissingCache).left(),
                    listOf(AmericanGangster).right()
                )
            })

        }
    ) { viewModel ->
        assert that viewModel.result.value `is` type<Success>()
    }

    @Test
    fun `does not deliver Error after Success, only if loading`() = viewModelTest(
        ignoreUnfinishedJobs = true,
        buildViewModel = {

            // Add some like movies for give more data to the test
            rateMovie(TheBookOfEli, UserRating.Positive)
            rateMovie(TheGreatDebaters, UserRating.Positive)

            ViewModel(getSuggestedMovies = mockk {
                var count = -1
                coEvery { this@mockk() } coAnswers {
                    count++
                    if (count > 0) throw Exception("Something has happened")
                    else flowOf(listOf(AmericanGangster).right())
                }
            })

        }
    ) { viewModel ->
        assert that viewModel.result.value `is` type<Success>()
        assert that fails {
            advanceUntilIdle()
            assert that viewModel.result.value `is` type<NoSuggestions>()
        }
    }

    data class MovieCount(val movie: Movie, val count: Int)

    operator fun GetSuggestedMovieViewModel.State?.plus(count: Int) = MovieCount((this as Success).movie, count)
}
