package cinescout.database

import arrow.core.Nel
import arrow.core.nonEmptyListOf
import cinescout.database.model.DatabaseEpisode
import cinescout.database.model.DatabaseSeason
import cinescout.database.model.DatabaseTvShow
import cinescout.database.sample.DatabaseEpisodeSample
import cinescout.database.sample.DatabaseSeasonSample
import cinescout.database.sample.DatabaseTvShowSample
import cinescout.test.database.TestDatabaseExtension
import cinescout.test.database.requireTestDatabaseExtension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class SeasonQueriesTest : BehaviorSpec({
    extensions(TestDatabaseExtension())

    Given("a tv show with seasons and episodes") {
        val tvShow = DatabaseTvShowSample.BreakingBad
        val seasons = nonEmptyListOf(
            DatabaseSeasonSample.BreakingBad_s0,
            DatabaseSeasonSample.BreakingBad_s1,
            DatabaseSeasonSample.BreakingBad_s2
        )
        val episodes = nonEmptyListOf(
            DatabaseEpisodeSample.BreakingBad_s0e1,
            DatabaseEpisodeSample.BreakingBad_s0e2,
            DatabaseEpisodeSample.BreakingBad_s0e3,
            DatabaseEpisodeSample.BreakingBad_s1e1,
            DatabaseEpisodeSample.BreakingBad_s1e2,
            DatabaseEpisodeSample.BreakingBad_s1e3,
            DatabaseEpisodeSample.BreakingBad_s2e1,
            DatabaseEpisodeSample.BreakingBad_s2e2,
            DatabaseEpisodeSample.BreakingBad_s2e3
        )
        val scenario = TestScenario()

        When("when inserted") {
            scenario.insert(tvShow, seasons, episodes)

            Then("all seasons and episodes can be found") {
                val result = scenario.sut.findAllSeasonsWithEpisodesByTraktTvShowId(tvShow.traktId).executeAsList()
                result shouldHaveSize 9
                result.map { it.seasonNumber to it.episodeNumber } shouldBe listOf(
                    0 to 1,
                    0 to 2,
                    0 to 3,
                    1 to 1,
                    1 to 2,
                    1 to 3,
                    2 to 1,
                    2 to 2,
                    2 to 3
                )
            }
        }
    }
})

private class SeasonQueriesTestScenario(
    private val database: Database
) {

    val sut: SeasonQueries = database.seasonQueries
    private val episodeQueries: EpisodeQueries = database.episodeQueries
    private val tvShowQueries: TvShowQueries = database.tvShowQueries

    suspend fun insert(
        tvShow: DatabaseTvShow,
        seasons: Nel<DatabaseSeason>,
        episodes: Nel<DatabaseEpisode>
    ) {
        tvShowQueries.insertTvShowObject(tvShow)
        for (season in seasons) {
            sut.insert(season)
        }
        for (episode in episodes) {
            episodeQueries.insert(episode)
        }
    }
}

private fun Spec.TestScenario(): SeasonQueriesTestScenario {
    val extension = requireTestDatabaseExtension()
    extension.clear()

    return SeasonQueriesTestScenario(extension.database)
}
