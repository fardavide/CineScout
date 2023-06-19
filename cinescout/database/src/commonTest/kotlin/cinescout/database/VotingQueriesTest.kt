package cinescout.database

import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseTvShow
import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.sample.DatabaseTmdbScreenplayIdSample
import cinescout.database.sample.DatabaseTraktScreenplayIdSample
import cinescout.database.sample.DatabaseTvShowSample
import cinescout.test.database.TestDatabaseExtension
import cinescout.test.database.requireTestDatabaseExtension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class VotingQueriesTest : BehaviorSpec({
    extensions(TestDatabaseExtension())

    Given("some voted screenplays") {

        When("screenplays are not cached") {
            val scenario = TestScenario().apply {
                sut.insert(
                    DatabaseTraktScreenplayIdSample.Inception,
                    DatabaseTmdbScreenplayIdSample.Inception,
                    isLiked = true
                )
                sut.insert(
                    DatabaseTraktScreenplayIdSample.BreakingBad,
                    DatabaseTmdbScreenplayIdSample.BreakingBad,
                    isLiked = true
                )
                sut.insert(
                    DatabaseTraktScreenplayIdSample.Grimm,
                    DatabaseTmdbScreenplayIdSample.Grimm,
                    isLiked = false
                )
                sut.insert(
                    DatabaseTraktScreenplayIdSample.War,
                    DatabaseTmdbScreenplayIdSample.War,
                    isLiked = false
                )
                insertMovie(DatabaseMovieSample.Inception)
                insertTvShow(DatabaseTvShowSample.BreakingBad)
            }

            Then("find all not fetched returns their ids") {
                val result = scenario.sut.findAllNotFetchedIds { tmdbId, traktId -> tmdbId to traktId }.executeAsList()
                result shouldBe listOf(
                    DatabaseTmdbScreenplayIdSample.Grimm to DatabaseTraktScreenplayIdSample.Grimm,
                    DatabaseTmdbScreenplayIdSample.War to DatabaseTraktScreenplayIdSample.War
                )
            }
        }
    }
})

private class VotingQueriesTestScenario(
    private val database: Database
) {

    val sut: VotingQueries = database.votingQueries

    fun insertMovie(movie: DatabaseMovie) {
        database.movieQueries.insertMovieObject(movie)
    }

    fun insertTvShow(tvShow: DatabaseTvShow) {
        database.tvShowQueries.insertTvShowObject(tvShow)
    }
}

private fun Spec.TestScenario(): VotingQueriesTestScenario {
    val extension = requireTestDatabaseExtension()
    extension.clear()

    return VotingQueriesTestScenario(extension.database)
}
