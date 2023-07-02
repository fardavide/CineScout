package cinescout.database

import cinescout.database.model.DatabaseHistory
import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseTvShow
import cinescout.database.model.id.DatabaseTmdbScreenplayId
import cinescout.database.model.id.DatabaseTraktScreenplayId
import cinescout.database.sample.DatabaseHistorySample
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
                scenario.findAllNotFetchedScreenplayIds() shouldBe listOf(
                    DatabaseTmdbScreenplayIdSample.War to DatabaseTraktScreenplayIdSample.War,
                    DatabaseTmdbScreenplayIdSample.Grimm to DatabaseTraktScreenplayIdSample.Grimm
                )
            }
        }
    }

    Given("Some tv shows in history") {

        When("tv shows are not cached") {
            val scenario = TestScenario().apply {
                insertHistory(DatabaseHistorySample.BreakingBad_s1e1)
                insertHistory(DatabaseHistorySample.BreakingBad_s1e2)
                insertHistory(DatabaseHistorySample.Grimm_s1e1)
                insertHistory(DatabaseHistorySample.Grimm_s1e2)
                insertHistory(DatabaseHistorySample.Grimm_s1e3)
                insertTvShow(DatabaseTvShowSample.BreakingBad)
            }

            Then("find all not fetched returns their ids") {
                scenario.findAllNotFetchedScreenplayIds() shouldBe listOf(
                    DatabaseTmdbScreenplayIdSample.Grimm to DatabaseTraktScreenplayIdSample.Grimm
                )
            }
        }
    }

    Given("Screenplay from different lists") {

        When("screenplays are not cached") {
            val scenario = TestScenario().apply {
                insertLiked(DatabaseTmdbScreenplayIdSample.Grimm, DatabaseTraktScreenplayIdSample.Grimm)
                insertDisliked(DatabaseTmdbScreenplayIdSample.Grimm, DatabaseTraktScreenplayIdSample.Grimm)
                insertHistory(DatabaseHistorySample.Grimm_s1e1)
                insertRated(DatabaseTmdbScreenplayIdSample.Grimm, DatabaseTraktScreenplayIdSample.Grimm, 8)
                insertWatchlist(DatabaseTmdbScreenplayIdSample.Grimm, DatabaseTraktScreenplayIdSample.Grimm)
            }

            Then("find all not fetched doesn't return duplicated ids") {
                scenario.findAllNotFetchedScreenplayIds() shouldBe listOf(
                    DatabaseTmdbScreenplayIdSample.Grimm to DatabaseTraktScreenplayIdSample.Grimm
                )
            }
        }
    }
})

private class SyncQueriesTestScenario(
    private val database: Database
) {

    fun findAllNotFetchedScreenplayIds(): List<Pair<DatabaseTmdbScreenplayId, DatabaseTraktScreenplayId>> =
        database.syncQueries.findAllNotFetchedScreenplayIds(::Pair).executeAsList()

    fun insertDisliked(tmdbId: DatabaseTmdbScreenplayId, traktId: DatabaseTraktScreenplayId) {
        database.votingQueries.insert(traktId, tmdbId, isLiked = false)
    }

    fun insertHistory(history: DatabaseHistory) {
        database.historyQueries.insert(history)
    }

    fun insertMovie(movie: DatabaseMovie) {
        database.movieQueries.insertMovieObject(movie)
    }

    fun insertLiked(tmdbId: DatabaseTmdbScreenplayId, traktId: DatabaseTraktScreenplayId) {
        database.votingQueries.insert(traktId, tmdbId, isLiked = true)
    }

    fun insertRated(
        tmdbId: DatabaseTmdbScreenplayId,
        traktId: DatabaseTraktScreenplayId,
        rating: Int
    ) {
        database.personalRatingQueries.insert(traktId, tmdbId, rating)
    }

    fun insertTvShow(tvShow: DatabaseTvShow) {
        database.tvShowQueries.insertTvShowObject(tvShow)
    }

    fun insertWatchlist(tmdbId: DatabaseTmdbScreenplayId, traktId: DatabaseTraktScreenplayId) {
        database.watchlistQueries.insertWatchlist(traktId, tmdbId)
    }
}

private fun Spec.TestScenario(): SyncQueriesTestScenario {
    val extension = requireTestDatabaseExtension()
    extension.clear()

    return SyncQueriesTestScenario(extension.database)
}
