package cinescout.database

import cinescout.database.model.DatabaseTmdbScreenplayId
import cinescout.database.sample.DatabaseTmdbScreenplayIdSample
import cinescout.test.database.TestDatabaseExtension
import cinescout.test.database.requireTestDatabaseExtension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize

class WatchlistQueriesTest : BehaviorSpec({
    extensions(TestDatabaseExtension())

    Given("two movies in watchlist") {
        val movie1 = DatabaseTmdbScreenplayIdSample.Inception
        val movie2 = DatabaseTmdbScreenplayIdSample.TheWolfOfWallStreet

        val scenario = TestScenario()
        scenario.insertWatchlist(movie1, movie2)

        When("delete one by id") {
            scenario.sut.deleteMovieById(movie1.value.toString())

            Then("only one movie is in watchlist") {
                val result = scenario.sut.findAllMovies().executeAsList()
                result shouldHaveSize 1
                result shouldContainExactly listOf(movie2)
            }
        }
    }
})

private class WatchlistQueriesTestScenario(
    private val database: Database
) {

    val sut: WatchlistQueries = database.watchlistQueries

    fun insertWatchlist(vararg screenplayIds: DatabaseTmdbScreenplayId) {
        for (screenplayId in screenplayIds) {
            database.watchlistQueries.insertWatchlist(screenplayId)
        }
    }
}

private fun Spec.TestScenario(): WatchlistQueriesTestScenario {
    val extension = requireTestDatabaseExtension()
    extension.clear()

    return WatchlistQueriesTestScenario(extension.database)
}
