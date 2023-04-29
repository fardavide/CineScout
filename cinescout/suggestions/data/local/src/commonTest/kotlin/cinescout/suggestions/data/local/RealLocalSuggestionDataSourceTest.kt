package cinescout.suggestions.data.local

import cinescout.database.MovieQueries
import cinescout.database.SuggestionQueries
import cinescout.database.TvShowQueries
import cinescout.database.model.DatabaseSuggestion
import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.sample.DatabaseSuggestionSample
import cinescout.database.sample.DatabaseTvShowSample
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.suggestions.data.local.mapper.FakeDatabaseSuggestionMapper
import cinescout.suggestions.domain.model.Affinity
import cinescout.suggestions.domain.model.SuggestionSourceType
import cinescout.suggestions.domain.sample.SuggestedScreenplaySample
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

    Given("suggestions") {

        When("get") {
            val scenario = TestScenario()
            scenario.sut.findAllSuggestionIds(ScreenplayType.All, SuggestionSourceType.values().toList())

            Then("find suggested movies ids on movie queries") {
                coVerify { scenario.suggestionQueries.findAllNotKnown() }
            }
        }

        And("suggestion doesn't exists") {

            When("insert") {
                val suggestedMovie = listOf(SuggestedScreenplaySample.Inception)
                val suggestedTvShow = listOf(SuggestedScreenplaySample.BreakingBad)
                val scenario = TestScenario()
                scenario.sut.insertSuggestedMovies(suggestedMovie)
                scenario.sut.insertSuggestedTvShows(suggestedTvShow)

                Then("movie is inserted") {
                    coVerify {
                        scenario.movieQueries.insertMovieObject(DatabaseMovieSample.Inception)
                    }
                }

                Then("tv show is inserted") {
                    coVerify {
                        scenario.tvShowQueries.insertTvShowObject(DatabaseTvShowSample.BreakingBad)
                    }
                }

                Then("suggestions are inserted") {
                    coVerify {
                        scenario.suggestionQueries.insert(DatabaseSuggestionSample.Inception)
                    }
                    coVerify {
                        scenario.suggestionQueries.insert(DatabaseSuggestionSample.BreakingBad)
                    }
                }
            }
        }

        And("suggestion exists") {
            val preexistingSuggestion = SuggestedScreenplaySample.Inception

            When("insert suggestion with higher affinity") {
                val updatedSuggestion = SuggestedScreenplaySample.Inception.copy(affinity = Affinity.of(100))
                val scenario = TestScenario()
                scenario.sut.insertSuggestedMovies(listOf(preexistingSuggestion))
                scenario.sut.insertSuggestedMovies(listOf(updatedSuggestion))

                Then("suggestion is inserted") {
                    coVerify(exactly = 2) { scenario.suggestionQueries.insert(any()) }
                }
            }

            When("insert suggestion with lower affinity") {
                val updatedSuggestion = SuggestedScreenplaySample.Inception.copy(affinity = Affinity.of(1))
                val scenario = TestScenario()
                scenario.sut.insertSuggestedMovies(listOf(preexistingSuggestion))
                scenario.sut.insertSuggestedMovies(listOf(updatedSuggestion))

                Then("suggestion is not inserted") {
                    coVerify(exactly = 1) { scenario.suggestionQueries.insert(any()) }
                }
            }
        }
    }
})

private class RealLocalSuggestionDataSourceTestScenario(
    val sut: RealLocalSuggestionDataSource,
    val movieQueries: MovieQueries,
    val suggestionQueries: SuggestionQueries,
    val tvShowQueries: TvShowQueries
)

private fun Spec.TestScenario(
    mappedDatabaseSuggestedMovie: DatabaseSuggestion = DatabaseSuggestionSample.Inception,
    mappedDatabaseSuggestedTvShow: DatabaseSuggestion = DatabaseSuggestionSample.BreakingBad
): RealLocalSuggestionDataSourceTestScenario {
    val testDatabaseExtension = requireTestDatabaseExtension()
    testDatabaseExtension.clear()

    val database = testDatabaseExtension.database
    val movieQueries = spyk(database.movieQueries)
    val suggestionQueries = spyk(database.suggestionQueries)
    val tvShowQueries = spyk(database.tvShowQueries)

    return RealLocalSuggestionDataSourceTestScenario(
        sut = RealLocalSuggestionDataSource(
            databaseSuggestionMapper = FakeDatabaseSuggestionMapper(
                databaseSuggestedMovie = mappedDatabaseSuggestedMovie,
                databaseSuggestedTvShow = mappedDatabaseSuggestedTvShow
            ),
            movieQueries = movieQueries,
            readDispatcher = StandardTestDispatcher(),
            writeDispatcher = newSingleThreadContext("write"),
            suggestionQueries = suggestionQueries,
            transacter = database,
            tvShowQueries = tvShowQueries
        ),
        movieQueries = movieQueries,
        suggestionQueries = suggestionQueries,
        tvShowQueries = tvShowQueries
    )
}
