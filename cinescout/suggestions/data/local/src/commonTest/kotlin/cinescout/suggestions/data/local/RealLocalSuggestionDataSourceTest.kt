package cinescout.suggestions.data.local

import cinescout.database.MovieQueries
import cinescout.database.SuggestedMovieQueries
import cinescout.database.SuggestedTvShowQueries
import cinescout.database.TvShowQueries
import cinescout.database.model.DatabaseSuggestedMovie
import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.sample.DatabaseSuggestedMovieSample
import cinescout.database.sample.DatabaseSuggestedTvShowSample
import cinescout.database.sample.DatabaseTvShowSample
import cinescout.suggestions.data.local.mapper.FakeDatabaseSuggestedMovieMapper
import cinescout.suggestions.data.local.mapper.FakeDatabaseSuggestedTvShowMapper
import cinescout.suggestions.domain.model.Affinity
import cinescout.suggestions.domain.sample.SuggestedMovieSample
import cinescout.suggestions.domain.sample.SuggestedTvShowSample
import cinescout.test.database.TestDatabaseExtension
import cinescout.test.database.requireTestDatabaseExtension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.StandardTestDispatcher

class RealLocalSuggestionDataSourceTest : BehaviorSpec({
    extension(TestDatabaseExtension())

    Given("suggested movies") {

        When("get") {
            val scenario = TestScenario()
            scenario.sut.findAllSuggestedMovies()

            Then("find suggested movies on movie queries") {
                coVerify { scenario.movieQueries.findAllSuggested() }
            }
        }

        And("suggestion doesn't exists") {

            When("insert") {
                val suggestedMovies = listOf(
                    SuggestedMovieSample.Inception
                )
                val scenario = TestScenario()
                scenario.sut.insertSuggestedMovies(suggestedMovies)

                Then("movie is inserted") {
                    coVerify { scenario.movieQueries.insertMovieObject(DatabaseMovieSample.Inception) }
                }

                Then("suggestion is inserted") {
                    coVerify { scenario.suggestedMovieQueries.insertSuggestion(DatabaseSuggestedMovieSample.Inception) }
                }
            }
        }

        And("suggestion exists") {
            val preexistingSuggestion = SuggestedMovieSample.Inception

            When("insert suggestion with higher affinity") {
                val updatedSuggestion = SuggestedMovieSample.Inception.copy(affinity = Affinity.of(100))
                val scenario = TestScenario()
                scenario.sut.insertSuggestedMovies(listOf(preexistingSuggestion))
                scenario.sut.insertSuggestedMovies(listOf(updatedSuggestion))

                Then("suggestion is inserted") {
                    coVerify(exactly = 2) { scenario.suggestedMovieQueries.insertSuggestion(any()) }
                }
            }

            When("insert suggestion with lower affinity") {
                val updatedSuggestion = SuggestedMovieSample.Inception.copy(affinity = Affinity.of(1))
                val scenario = TestScenario()
                scenario.sut.insertSuggestedMovies(listOf(preexistingSuggestion))
                scenario.sut.insertSuggestedMovies(listOf(updatedSuggestion))

                Then("suggestion is not inserted") {
                    coVerify(exactly = 1) { scenario.suggestedMovieQueries.insertSuggestion(any()) }
                }
            }
        }
    }

    Given("suggested tv shows") {

        When("get") {
            val scenario = TestScenario()
            scenario.sut.findAllSuggestedTvShows()

            Then("find suggested tv shows on movie queries") {
                coVerify { scenario.tvShowQueries.findAllSuggested() }
            }
        }

        And("suggestion doesn't exist") {

            When("insert") {
                val suggestedTvShows = listOf(
                    SuggestedTvShowSample.Grimm
                )
                val scenario = TestScenario()
                scenario.sut.insertSuggestedTvShows(suggestedTvShows)

                Then("tv show is inserted") {
                    coVerify { scenario.tvShowQueries.insertTvShowObject(DatabaseTvShowSample.Grimm) }
                }

                Then("suggestion is inserted") {
                    coVerify { scenario.suggestedTvShowQueries.insertSuggestion(DatabaseSuggestedTvShowSample.Grimm) }
                }
            }
        }

        And("suggestion exists") {
            val preexistingSuggestion = SuggestedTvShowSample.Grimm

            When("insert suggestion with higher affinity") {
                val updatedSuggestion = SuggestedTvShowSample.Grimm.copy(affinity = Affinity.of(100))
                val scenario = TestScenario()
                scenario.sut.insertSuggestedTvShows(listOf(preexistingSuggestion))
                scenario.sut.insertSuggestedTvShows(listOf(updatedSuggestion))

                Then("suggestion is inserted") {
                    coVerify(exactly = 2) { scenario.suggestedTvShowQueries.insertSuggestion(any()) }
                }
            }

            When("insert suggestion with lower affinity") {
                val updatedSuggestion = SuggestedTvShowSample.Grimm.copy(affinity = Affinity.of(1))
                val scenario = TestScenario()
                scenario.sut.insertSuggestedTvShows(listOf(preexistingSuggestion))
                scenario.sut.insertSuggestedTvShows(listOf(updatedSuggestion))

                Then("suggestion is not inserted") {
                    coVerify(exactly = 1) { scenario.suggestedTvShowQueries.insertSuggestion(any()) }
                }
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

private fun Spec.TestScenario(
    mappedDatabaseSuggestedMovie: DatabaseSuggestedMovie = DatabaseSuggestedMovieSample.Inception
): RealLocalSuggestionDataSourceTestScenario {
    val testDatabaseExtension = requireTestDatabaseExtension()
    testDatabaseExtension.clear()

    val database = testDatabaseExtension.database
    val movieQueries = spyk(database.movieQueries)
    val suggestedMovieQueries = spyk(database.suggestedMovieQueries)
    val suggestedTvShowQueries = spyk(database.suggestedTvShowQueries)
    val tvShowQueries = spyk(database.tvShowQueries)

    return RealLocalSuggestionDataSourceTestScenario(
        sut = RealLocalSuggestionDataSource(
            databaseSuggestedMovieMapper = FakeDatabaseSuggestedMovieMapper(
                databaseSuggestedMovie = mappedDatabaseSuggestedMovie
            ),
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
