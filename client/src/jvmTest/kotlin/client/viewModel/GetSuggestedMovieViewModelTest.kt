package client.viewModel

import assert4k.*
import client.ViewState.Error
import client.ViewState.Success
import client.awaitData
import client.util.ViewModelTest
import client.viewModel.GetSuggestedMovieViewModel.Error.NoRatedMovies
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
import domain.stats.GetSuggestedMovies
import domain.stats.GetSuggestionData
import domain.stats.RateMovie
import entities.field.UserRating
import entities.movies.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlin.test.*

internal class GetSuggestedMovieViewModelTest : ViewModelTest {

    private val stats = MockStatRepository()
    private val rateMovie = RateMovie(stats)
    private val mockAddMovieToWatchlist = mockk<AddMovieToWatchlist>(relaxed = true)

    private fun CoroutineScope.ViewModel(
        randomize: Boolean = false,
        getSuggestedMovies: GetSuggestedMovies = GetSuggestedMovies(
            discover = DiscoverMovies(MockMovieRepository()),
            generateDiscoverParams = GenerateDiscoverParams(randomize = randomize),
            getSuggestionsData = GetSuggestionData(stats),
            stats = stats
        ),
        addMovieToWatchlist: AddMovieToWatchlist = AddMovieToWatchlist(stats),
    ) = GetSuggestedMovieViewModel(
        this,
        dispatchers,
        getSuggestedMovies,
        addMovieToWatchlist,
        rateMovie,
    )

    @Test
    fun `Can catch exceptions and deliver with result`() = viewModelTest({

        ViewModel(getSuggestedMovies = mockk {
            coEvery { this@mockk(any<Int>()) } answers {
                throw Exception("Something has happened")
            }
        })

    }) { viewModel ->

        assert that viewModel.result.state `is` type<Error>()
    }

    @Test
    fun `Can deliver suggested movie`() = viewModelTest({

        // Add some like movies for give more data to the test
        rateMovie(Inception, UserRating.Positive)
        rateMovie(TheBookOfEli, UserRating.Positive)
        rateMovie(TheGreatDebaters, UserRating.Positive)

        ViewModel()

    }) { viewModel ->

        assert that viewModel.result.state `is` type<Success<Movie>>()
    }

    @Test
    fun `can add movie to watchlist`() = viewModelTest({

        // Add some like movies for give more data to the test
        rateMovie(DejaVu, UserRating.Positive)
        rateMovie(Inception, UserRating.Positive)
        rateMovie(TheGreatDebaters, UserRating.Positive)

        ViewModel(addMovieToWatchlist = mockAddMovieToWatchlist)

    }) { viewModel ->

        val movie = viewModel.result.data!!
        viewModel.addCurrentToWatchlist()

        coVerify { mockAddMovieToWatchlist(movie) }
    }

    @Test
    fun `Skip shown next movie`() = viewModelTest({

        // Add some like movies for give more data to the test
        rateMovie(DejaVu, UserRating.Positive)
        rateMovie(Inception, UserRating.Positive)
        rateMovie(TheGreatDebaters, UserRating.Positive)

        ViewModel()

    }) { viewModel ->

        val first = viewModel.result.state
        assert that first `is` type<Success<Movie>>()

        viewModel.skipCurrent()
        val second = viewModel.result.data
        assert that second *{
            it `is not` Null
            it `not equals` first.data
        }
    }

    @Test
    fun `Does not stop delivering suggestions`() = viewModelTest({

        // Add some like movies for give more data to the test
        rateMovie(Inception, UserRating.Positive)
        rateMovie(TheBookOfEli, UserRating.Positive)
        rateMovie(TheGreatDebaters, UserRating.Positive)

        ViewModel()

    }) { viewModel ->

        var last: MovieCount? = null

        repeat(100) { count ->
            val current = viewModel.result.data
            assert that current * {
                it `is not` Null { "Error on iteration #$count" }
                +(this + count) `not equals` last
            }
            last = current + count
            viewModel.skipCurrent()
        }
    }

    @Test
    fun `Does not show liked movies`() = viewModelTest({

        // Add some like movies for give more data to the test
        rateMovie(TheBookOfEli, UserRating.Positive)
        rateMovie(TheGreatDebaters, UserRating.Positive)

        ViewModel()

    }) { viewModel ->

        val current = viewModel.result.data
        assert that current `is` type<Movie>()

        viewModel.likeCurrent()

        repeat(100) { count ->
            assert that viewModel.result.data * {
                it `is not` Null { "Error on iteration #$count" }
                it `not equals` current { "Error on iteration #$count" }
            }
            viewModel.skipCurrent()
        }
    }

    @Test
    fun `does not show disliked movies`() = viewModelTest({

        // Add some like movies for give more data to the test
        rateMovie(TheBookOfEli, UserRating.Positive)
        rateMovie(TheGreatDebaters, UserRating.Positive)

        ViewModel()

    }) { viewModel ->

        val current = viewModel.result.data
        assert that current `is` type<Movie>()

        viewModel.dislikeCurrent()

        repeat(100) { count ->
            assert that viewModel.result.data * {
                it `is not` Null { "Error on iteration #$count" }
                it `not equals` current { "Error on iteration #$count" }
            }
            viewModel.skipCurrent()
        }
    }

    @Test
    fun `show NoRatedMovies if no movie has been rated`() = viewModelTest({
        ViewModel()
    }) { viewModel ->
        assert that viewModel.result.state equals NoRatedMovies
    }

    @Test
    fun `can recover after error`() = viewModelTest({

        ViewModel(getSuggestedMovies = mockk {
            var count = -1
            coEvery { this@mockk(any<Int>()) } coAnswers  {
                count++
                if (count > 0) listOf(AmericanGangster)
                else throw Exception("Something has happened")
            }
        })

    }) { viewModel ->
        assert that viewModel.result.state `is` type<Error>()
        assert that viewModel.result.awaitData() `is` type<Movie>()
    }

    @Test
    fun `does not deliver Error after Success, only if loading`() = viewModelTest({

        // Add some like movies for give more data to the test
        rateMovie(TheBookOfEli, UserRating.Positive)
        rateMovie(TheGreatDebaters, UserRating.Positive)

        ViewModel(getSuggestedMovies = mockk {
            var count = -1
            coEvery { this@mockk(any<Int>()) } coAnswers  {
                count++
                if (count > 0) throw Exception("Something has happened")
                else listOf(AmericanGangster)
            }
        })

    }) { viewModel ->
        assert that viewModel.result.data `is` type<Movie>()
        assert that fails {
            advanceUntilIdle()
            assert that viewModel.result.state `is` type<Error>()
        }
    }

    data class MovieCount(val movie: Movie, val count: Int,)
    operator fun Movie?.plus(count: Int) = MovieCount(this!!, count)
}
