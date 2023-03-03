package cinescout.suggestions.data.local

import cinescout.database.MovieQueries
import cinescout.database.SuggestedMovieQueries
import cinescout.database.SuggestedTvShowQueries
import cinescout.database.TvShowQueries
import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.sample.DatabaseSuggestedMovieSample
import cinescout.database.sample.DatabaseSuggestedTvShowSample
import cinescout.database.sample.DatabaseTvShowSample
import cinescout.suggestions.data.local.mapper.FakeDatabaseSuggestedMovieMapper
import cinescout.suggestions.data.local.mapper.FakeDatabaseSuggestedTvShowMapper
import cinescout.suggestions.domain.sample.SuggestedMovieSample
import cinescout.suggestions.domain.sample.SuggestedTvShowSample
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

        When("insert") {
            val suggestedTvShows = listOf(
                SuggestedTvShowSample.Grimm
            )
            scenario.sut.insertSuggestedTvShows(suggestedTvShows)

            Then("tv shows are inserted") {
                coVerify { scenario.tvShowQueries.insertTvShowObject(DatabaseTvShowSample.Grimm) }
            }

            Then("suggestions are inserted") {
                coVerify { scenario.suggestedTvShowQueries.insertSuggestion(DatabaseSuggestedTvShowSample.Grimm) }
            }
        }
    }
})

private class RealLocalSuggestionDataSourceTestScenario(
    val sut: RealLocalSuggestionDataSource,
    val movieQueries: MovieQueries,
    val suggestedMovieQueries: SuggestedMovieQueries,
    val suggestedTvShowQueries: SuggestedTvShowQueries,
    val tvShowQueries: TvShowQueries
)

private fun Spec.TestScenario(): RealLocalSuggestionDataSourceTestScenario {
    val testDatabaseExtension = registeredExtensions().filterIsInstance<TestDatabaseExtension>().first()

    val database = testDatabaseExtension.database
    val movieQueries = spyk(database.movieQueries)
    val suggestedMovieQueries = spyk(database.suggestedMovieQueries)
    val suggestedTvShowQueries = spyk(database.suggestedTvShowQueries)
    val tvShowQueries = spyk(database.tvShowQueries)

    @Suppress("OPT_IN_USAGE")
    return RealLocalSuggestionDataSourceTestScenario(
        sut = RealLocalSuggestionDataSource(
            databaseSuggestedMovieMapper = FakeDatabaseSuggestedMovieMapper(),
            databaseSuggestedTvShowMapper = FakeDatabaseSuggestedTvShowMapper(),
            movieQueries = movieQueries,
            readDispatcher = StandardTestDispatcher(),
            writeDispatcher = newSingleThreadContext("write"),
            suggestedMovieQueries = suggestedMovieQueries,
            suggestedTvShowQueries = suggestedTvShowQueries,
            transacter = database,
            tvShowQueries = tvShowQueries
        ),
        movieQueries = movieQueries,
        suggestedMovieQueries = suggestedMovieQueries,
        suggestedTvShowQueries = suggestedTvShowQueries,
        tvShowQueries = tvShowQueries
    )
}
