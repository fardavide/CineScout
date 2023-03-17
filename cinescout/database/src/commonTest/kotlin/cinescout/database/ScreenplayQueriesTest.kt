package cinescout.database

import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseTvShow
import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.sample.DatabaseScreenplaySample
import cinescout.database.sample.DatabaseTvShowSample
import cinescout.test.database.TestDatabaseExtension
import cinescout.test.database.requireTestDatabaseExtension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize

class ScreenplayQueriesTest : BehaviorSpec({
    extensions(TestDatabaseExtension())

    Given("a disliked movie") {
        val dislikedMovie = DatabaseMovieSample.Inception

        And("a disliked tv show") {
            val dislikedTvShow = DatabaseTvShowSample.BreakingBad

            When("finding all disliked screenplays") {
                val scenario = TestScenario()
                scenario.insertDislikes(dislikedMovie, dislikedTvShow)
                val result = scenario.sut.findAllDisliked().executeAsList()

                Then("two items are returned") {
                    result shouldHaveSize 2
                }

                Then("the disliked movie and tv show are returned") {
                    result shouldContainExactlyInAnyOrder listOf(
                        DatabaseScreenplaySample.Inception,
                        DatabaseScreenplaySample.BreakingBad
                    )
                }
            }
        }
    }

    Given("a watchlist movie") {
        val watchlistMovie = DatabaseMovieSample.Memento

        And("and a watchlist tv show with same id") {
            val watchlistTvShow = DatabaseTvShowSample.TVPatrolNorthernLuzon

            When("finding all watchlist screenplays") {
                val scenario = TestScenario()
                scenario.insertWatchlist(watchlistMovie, watchlistTvShow)
                val result = scenario.sut.findAllWatchlist().executeAsList()

                Then("the two items are returned") {
                    result shouldHaveSize 2
                }

                Then("the watchlist movie and tv show are returned") {
                    result shouldContainExactlyInAnyOrder listOf(
                        DatabaseScreenplaySample.Memento,
                        DatabaseScreenplaySample.TVPatrolNorthernLuzon
                    )
                }
            }
        }
    }
})

private class ScreenplayQueriesTestScenario(
    private val database: Database
) {

    val sut: ScreenplayQueries = database.screenplayQueries

    fun insertDislikes(vararg screenplays: Any) {
        for (screenplay in screenplays) {
            when (screenplay) {
                is DatabaseMovie -> {
                    database.movieQueries.insertMovieObject(screenplay)
                    database.votingQueries.insert(screenplay.tmdbId, isLiked = false)
                }
                is DatabaseTvShow -> {
                    database.tvShowQueries.insertTvShowObject(screenplay)
                    database.votingQueries.insert(screenplay.tmdbId, isLiked = false)
                }
                else -> error("Unknown screenplay: $screenplay")
            }
        }
    }

    fun insertWatchlist(vararg screenplays: Any) {
        for (screenplay in screenplays) {
            when (screenplay) {
                is DatabaseMovie -> {
                    database.movieQueries.insertMovieObject(screenplay)
                    database.watchlistQueries.insertWatchlist(screenplay.tmdbId)
                }
                is DatabaseTvShow -> {
                    database.tvShowQueries.insertTvShowObject(screenplay)
                    database.watchlistQueries.insertWatchlist(screenplay.tmdbId)
                }
                else -> error("Unknown screenplay: $screenplay")
            }
        }
    }
}

private fun Spec.TestScenario(): ScreenplayQueriesTestScenario {
    val extension = requireTestDatabaseExtension()
    extension.clear()

    return ScreenplayQueriesTestScenario(extension.database)
}
