package cinescout.database

import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseTvShow
import cinescout.database.model.id.DatabaseTmdbScreenplayId
import cinescout.database.model.id.DatabaseTraktScreenplayId
import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.sample.DatabaseTmdbScreenplayIdSample
import cinescout.database.sample.DatabaseTraktScreenplayIdSample
import cinescout.database.sample.DatabaseTvShowSample
import cinescout.test.database.TestDatabaseExtension
import cinescout.test.database.requireTestDatabaseExtension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class SyncQueriesTest : BehaviorSpec({
    extensions(TestDatabaseExtension())

    Given("some voted screenplays") {

        When("screenplays are not cached") {
            val scenario = TestScenario().apply {
                insertLiked(
                    DatabaseTmdbScreenplayIdSample.Inception,
                    DatabaseTraktScreenplayIdSample.Inception
                )
                insertLiked(
                    DatabaseTmdbScreenplayIdSample.BreakingBad,
                    DatabaseTraktScreenplayIdSample.BreakingBad
                )
                insertDisliked(
                    DatabaseTmdbScreenplayIdSample.Grimm,
                    DatabaseTraktScreenplayIdSample.Grimm
                )
                insertDisliked(
                    DatabaseTmdbScreenplayIdSample.War,
                    DatabaseTraktScreenplayIdSample.War
                )
                insertMovie(DatabaseMovieSample.Inception)
                insertTvShow(DatabaseTvShowSample.BreakingBad)
            }

            Then("find all not fetched returns their ids") {
                val result = scenario.sut.findAllNotFetchedScreenplayIds { tmdbId, traktId -> tmdbId to traktId }
                    .executeAsList()
                result shouldBe listOf(
                    DatabaseTmdbScreenplayIdSample.War to DatabaseTraktScreenplayIdSample.War,
                    DatabaseTmdbScreenplayIdSample.Grimm to DatabaseTraktScreenplayIdSample.Grimm
                )
            }
        }
    }
})

private class SyncQueriesTestScenario(
    private val database: Database
) {

    val sut: SyncQueries = database.syncQueries

    fun insertDisliked(tmdbId: DatabaseTmdbScreenplayId, traktId: DatabaseTraktScreenplayId) {
        database.votingQueries.insert(traktId, tmdbId, isLiked = false)
    }

    fun insertMovie(movie: DatabaseMovie) {
        database.movieQueries.insertMovieObject(movie)
    }

    fun insertLiked(tmdbId: DatabaseTmdbScreenplayId, traktId: DatabaseTraktScreenplayId) {
        database.votingQueries.insert(traktId, tmdbId, isLiked = true)
    }

    fun insertTvShow(tvShow: DatabaseTvShow) {
        database.tvShowQueries.insertTvShowObject(tvShow)
    }
}

private fun Spec.TestScenario(): SyncQueriesTestScenario {
    val extension = requireTestDatabaseExtension()
    extension.clear()

    return SyncQueriesTestScenario(extension.database)
}
