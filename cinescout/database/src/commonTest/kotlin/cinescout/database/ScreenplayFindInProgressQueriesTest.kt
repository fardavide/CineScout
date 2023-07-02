package cinescout.database

import cinescout.database.model.DatabaseHistory
import cinescout.database.model.DatabaseTvShow
import cinescout.database.model.DatabaseTvShowStatus
import cinescout.database.model.id.DatabaseHistoryItemId
import cinescout.database.sample.DatabaseHistorySample
import cinescout.database.sample.DatabaseScreenplaySample
import cinescout.database.sample.DatabaseTvShowSample
import cinescout.test.database.TestDatabaseExtension
import cinescout.test.database.requireTestDatabaseExtension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class ScreenplayFindInProgressQueriesTest : BehaviorSpec({
    extensions(TestDatabaseExtension())

    Given("two tv show") {

        When("no history") {
            val scenario = TestScenario().apply {
                insert(DatabaseTvShowSample.BreakingBad)
                insert(DatabaseTvShowSample.Grimm)
            }

            Then("return empty list") {
                scenario.find() shouldBe emptyList()
            }
        }

        When("one has history") {
            val scenario = TestScenario().apply {
                insert(DatabaseTvShowSample.BreakingBad)
                insert(DatabaseTvShowSample.Grimm)
                insert(DatabaseHistorySample.BreakingBad_s1e1)
            }

            Then("return one tv show") {
                scenario.find() shouldBe listOf(
                    DatabaseScreenplaySample.BreakingBad
                )
            }
        }

        When("multiple history items") {
            val scenario = TestScenario().apply {
                insert(DatabaseTvShowSample.BreakingBad)
                insert(DatabaseTvShowSample.Grimm)
                insert(DatabaseHistorySample.BreakingBad_s1e1)
                insert(DatabaseHistorySample.BreakingBad_s1e2)
                insert(DatabaseHistorySample.Grimm_s1e1)
                insert(DatabaseHistorySample.Grimm_s1e2)
                insert(DatabaseHistorySample.Grimm_s1e3)
            }

            Then("return both tv shows not duplicated") {
                scenario.find() shouldBe listOf(
                    DatabaseScreenplaySample.BreakingBad,
                    DatabaseScreenplaySample.Grimm
                )
            }
        }
    }

    Given("one tv show") {

        When("all episodes watched") {
            val scenario = TestScenario().apply {
                val tvShow = DatabaseTvShowSample.Grimm.copy(
                    airedEpisodes = 3,
                    status = DatabaseTvShowStatus.Ended
                )
                insert(tvShow)
                insert(DatabaseHistorySample.Grimm_s1e1)
                insert(DatabaseHistorySample.Grimm_s1e2)
                insert(DatabaseHistorySample.Grimm_s1e3)
            }

            Then("return empty list") {
                scenario.find() shouldBe emptyList()
            }
        }

        When("multiple history but not all watched") {
            val scenario = TestScenario().apply {
                val tvShow = DatabaseTvShowSample.Grimm.copy(
                    airedEpisodes = 3,
                    status = DatabaseTvShowStatus.Ended
                )
                insert(tvShow)
                insert(DatabaseHistorySample.Grimm_s1e1)
                insert(DatabaseHistorySample.Grimm_s1e2)
                insert(DatabaseHistorySample.Grimm_s1e2.copy(itemId = DatabaseHistoryItemId(999)))
            }

            Then("return tv show") {
                scenario.find() shouldHaveSize 1
            }
        }

        When("all episodes watched but not ended") {
            val scenario = TestScenario().apply {
                val tvShow = DatabaseTvShowSample.Grimm.copy(
                    airedEpisodes = 3,
                    status = DatabaseTvShowStatus.Continuing
                )
                insert(tvShow)
                insert(DatabaseHistorySample.Grimm_s1e1)
                insert(DatabaseHistorySample.Grimm_s1e2)
                insert(DatabaseHistorySample.Grimm_s1e3)
            }

            Then("return tv show") {
                scenario.find() shouldHaveSize 1
            }
        }
    }
})

private class ScreenplayFindInProgressQueriesTestScenario(
    val database: Database
) {

    fun find(): List<Screenplay> = database.screenplayFindInProgressQueries.all().executeAsList()

    fun insert(history: DatabaseHistory) {
        database.historyQueries.insert(history)
    }

    fun insert(tvShow: DatabaseTvShow) {
        database.tvShowQueries.insertTvShowObject(tvShow)
    }
}

private fun Spec.TestScenario(): ScreenplayFindInProgressQueriesTestScenario {
    val extension = requireTestDatabaseExtension()
    extension.clear()

    return ScreenplayFindInProgressQueriesTestScenario(extension.database)
}
