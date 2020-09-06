package client.viewModel

import assert4k.*
import client.ViewState.Error
import client.ViewState.Success
import client.util.ViewModelTest
import domain.DiscoverMovies
import domain.GenerateDiscoverParams
import domain.GetSuggestedMovies
import domain.GetSuggestionData
import domain.MockMovieRepository
import domain.MockStatRepository
import domain.RateMovie
import domain.Test.Movie.DejaVu
import domain.Test.Movie.Inception
import domain.Test.Movie.TheBookOfEli
import domain.Test.Movie.TheGreatDebaters
import entities.Rating
import entities.movies.Movie
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlin.test.*

internal class GetSuggestedMovieViewModelTest : ViewModelTest {

    private val stats = MockStatRepository()
    private val rateMovie = RateMovie(stats)
    private fun CoroutineScope.ViewModel(
        randomize: Boolean = true,
        getSuggestedMovies: GetSuggestedMovies = GetSuggestedMovies(
            discover = DiscoverMovies(MockMovieRepository()),
            generateDiscoverParams = GenerateDiscoverParams(randomize = randomize),
            getSuggestionsData = GetSuggestionData(stats),
            stats = stats
        ),
    ): GetSuggestedMovieViewModel {
        return GetSuggestedMovieViewModel(
            this,
            dispatchers,
            getSuggestedMovies,
            rateMovie
        )
    }

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
        rateMovie(Inception, Rating.Positive)
        rateMovie(TheBookOfEli, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)

        ViewModel(randomize = false)

    }) { viewModel ->

        assert that viewModel.result.state `is` type<Success<Movie>>()
    }

    @Test
    fun `Skip shown next movie`() = viewModelTest({

        // Add some like movies for give more data to the test
        rateMovie(DejaVu, Rating.Positive)
        rateMovie(Inception, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)

        ViewModel(randomize = false)

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
        rateMovie(Inception, Rating.Positive)
        rateMovie(TheBookOfEli, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)

        ViewModel(randomize = false)

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
        rateMovie(TheBookOfEli, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)

        ViewModel(randomize = false)

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
    fun `Does not show disliked movies`() = viewModelTest({

        // Add some like movies for give more data to the test
        rateMovie(TheBookOfEli, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)

        ViewModel(randomize = false)

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

    data class MovieCount(val movie: Movie, val count: Int,)
    operator fun Movie?.plus(count: Int) = MovieCount(this!!, count)
}
