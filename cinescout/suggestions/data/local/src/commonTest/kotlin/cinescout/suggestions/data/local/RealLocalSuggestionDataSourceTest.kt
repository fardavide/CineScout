package cinescout.suggestions.data.local

import cinescout.database.MovieQueries
import cinescout.database.SuggestedMovieQueries
import cinescout.database.TvShowQueries
import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.sample.DatabaseSuggestedMovieSample
import cinescout.suggestions.data.local.mapper.FakeDatabaseSuggestedMovieMapper
import cinescout.suggestions.domain.sample.SuggestedMovieSample
import cinescout.test.database.TestDatabaseExtension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.StandardTestDispatcher

class RealLocalSuggestionDataSourceTest : BehaviorSpec({
    extension(TestDatabaseExtension())

    val scenario = TestScenario()

    Given("suggested movies") {

        When("get") {
            scenario.sut.findAllSuggestedMovies()

            Then("find suggested movies on movie queries") {
                coVerify { scenario.movieQueries.findAllSuggested() }
            }
        }

        When("insert") {
            val suggestedMovies = listOf(
                SuggestedMovieSample.Inception
            )
            scenario.sut.insertSuggestedMovies(suggestedMovies)

            Then("movies are inserted") {
                coVerify { scenario.movieQueries.insertMovieObject(DatabaseMovieSample.Inception) }
            }

            Then("suggestions are inserted") {
                coVerify { scenario.suggestedMovieQueries.insertSuggestion(DatabaseSuggestedMovieSample.Inception) }
            }
        }
    }

    Given("suggested tv shows") {

        When("get") {
            scenario.sut.findAllSuggestedTvShows()

            Then("find suggested tv shows on movie queries") {
                coVerify { scenario.tvShowQueries.findAllSuggested() }
            }
        }
    }
})

private class RealLocalSuggestionDataSourceTestScenario(
    val sut: RealLocalSuggestionDataSource,
    val movieQueries: MovieQueries,
    val suggestedMovieQueries: SuggestedMovieQueries,
    val tvShowQueries: TvShowQueries
)

private fun Spec.TestScenario(): RealLocalSuggestionDataSourceTestScenario {
    val testDatabaseExtension = registeredExtensions().filterIsInstance<TestDatabaseExtension>().first()

    val database = testDatabaseExtension.database
    val movieQueries = spyk(database.movieQueries)
    val suggestedMovieQueries = spyk(database.suggestedMovieQueries)
    val tvShowQueries = spyk(database.tvShowQueries)

    @Suppress("OPT_IN_USAGE")
    return RealLocalSuggestionDataSourceTestScenario(
        sut = RealLocalSuggestionDataSource(
            transacter = database,
            databaseSuggestedMovieMapper = FakeDatabaseSuggestedMovieMapper(),
            movieQueries = movieQueries,
            readDispatcher = StandardTestDispatcher(),
            writeDispatcher = newSingleThreadContext("write"),
            suggestedMovieQueries = suggestedMovieQueries,
            tvShowQueries = tvShowQueries
        ),
        movieQueries = movieQueries,
        suggestedMovieQueries = suggestedMovieQueries,
        tvShowQueries = tvShowQueries
    )
}
