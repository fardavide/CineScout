package cinescout.database

import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseTvShow
import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.sample.DatabaseScreenplaySample
import cinescout.database.sample.DatabaseScreenplayWithPersonalRatingSample
import cinescout.database.sample.DatabaseTvShowSample
import cinescout.test.database.TestDatabaseExtension
import cinescout.test.database.requireTestDatabaseExtension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class ScreenplayQueriesTest : BehaviorSpec({
    extensions(TestDatabaseExtension())

    Given("a disliked movie") {
        val dislikedMovie = DatabaseMovieSample.Inception

        And("a disliked tv show") {
            val dislikedTvShow = DatabaseTvShowSample.BreakingBad

            When("finding all disliked screenplays") {
                val scenario = TestScenario()
                scenario.insertDislikes(dislikedMovie, dislikedTvShow)
                val result = scenario.findAllDislikedQueries.all().executeAsList()

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

    Given("a rated movie") {
        val ratedMovie = DatabaseMovieSample.Inception to
            DatabaseScreenplayWithPersonalRatingSample.Inception.personalRating

        And("a rated tv show") {
            val ratedTvShow = DatabaseTvShowSample.BreakingBad to
                DatabaseScreenplayWithPersonalRatingSample.BreakingBad.personalRating

            When("finding all rated screenplays") {
                val scenario = TestScenario()
                scenario.insertRatings(ratedMovie, ratedTvShow)
                val result = scenario.findWithPersonalRatingQueries.all().executeAsList()

                Then("two items are returned") {
                    result shouldHaveSize 2
                }

                Then("the rated movie and tv show are returned") {
                    result shouldContainExactlyInAnyOrder listOf(
                        DatabaseScreenplayWithPersonalRatingSample.Inception,
                        DatabaseScreenplayWithPersonalRatingSample.BreakingBad
                    )
                }
            }
        }
    }

    Given("a watchlist movie") {
        val watchlistMovie = DatabaseMovieSample.Memento

        And("and a watchlist tv show with same id") {
            val watchlistTvShow = DatabaseTvShowSample.TVPatrolNorthernLuzon
            val scenario = TestScenario()
            scenario.insertWatchlist(watchlistMovie, watchlistTvShow)

            When("finding all watchlist screenplays") {
                val result = scenario.findWatchlistQueries.all().executeAsList()

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

            When("finding all the watchlist movies") {
                val result = scenario.findWatchlistQueries.allMovies().executeAsList()

                Then("the watchlist movie is returned") {
                    result shouldBe listOf(
                        DatabaseScreenplaySample.Memento
                    )
                }
            }

            When("finding all the watchlist tv shows") {
                val result = scenario.findWatchlistQueries.allTvShows().executeAsList()

                Then("the watchlist tv show is returned") {
                    result shouldBe listOf(
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

    val findAllDislikedQueries: ScreenplayFindDislikedQueries = database.screenplayFindDislikedQueries
    val findWatchlistQueries: ScreenplayFindWatchlistQueries = database.screenplayFindWatchlistQueries
    val findWithPersonalRatingQueries: ScreenplayFindWithPersonalRatingQueries =
        database.screenplayFindWithPersonalRatingQueries

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

    fun insertRatings(vararg screenplays: Pair<Any, Int>) {
        for ((screenplay, rating) in screenplays) {
            when (screenplay) {
                is DatabaseMovie -> {
                    database.movieQueries.insertMovieObject(screenplay)
                    database.personalRatingQueries.insert(screenplay.tmdbId, rating)
                }
                is DatabaseTvShow -> {
                    database.tvShowQueries.insertTvShowObject(screenplay)
                    database.personalRatingQueries.insert(screenplay.tmdbId, rating)
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
