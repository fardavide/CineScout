package cinescout.database

import cinescout.database.ext.ids
import cinescout.database.model.DatabaseScreenplayIds
import cinescout.database.sample.DatabaseScreenplayIdsSample
import cinescout.test.database.TestDatabaseExtension
import cinescout.test.database.requireTestDatabaseExtension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class WatchlistQueriesTest : BehaviorSpec({
    extensions(TestDatabaseExtension())

    Given("two movies in watchlist") {
        val movie1 = DatabaseScreenplayIdsSample.Inception
        val movie2 = DatabaseScreenplayIdsSample.TheWolfOfWallStreet

        val scenario = TestScenario()
        scenario.insertWatchlist(movie1, movie2)

        When("delete one by id") {
            scenario.sut.deleteMovieById(movie1.tmdb.value.toString())

            Then("only one movie is in watchlist") {
                val result = scenario.sut.findAllMovies().executeAsList()
                result shouldHaveSize 1
                result.map { it.ids } shouldBe listOf(movie2)
            }
        }
    }
})

private class WatchlistQueriesTestScenario(
    private val database: Database
) {

    val sut: WatchlistQueries = database.watchlistQueries

    fun insertWatchlist(vararg screenplayIds: DatabaseScreenplayIds) {
        for (screenplayId in screenplayIds) {
            database.watchlistQueries.insertWatchlist(screenplayId.trakt, screenplayId.tmdb)
        }
    }
}

private fun Spec.TestScenario(): WatchlistQueriesTestScenario {
    val extension = requireTestDatabaseExtension()
    extension.clear()

    return WatchlistQueriesTestScenario(extension.database)
}
