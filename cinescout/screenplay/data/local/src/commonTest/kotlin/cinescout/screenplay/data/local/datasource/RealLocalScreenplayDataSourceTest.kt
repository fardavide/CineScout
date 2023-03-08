package cinescout.screenplay.data.local.datasource

import cinescout.database.RecommendationQueries
import cinescout.database.sample.DatabaseTmdbScreenplayIdSample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.test.database.TestDatabaseExtension
import cinescout.test.database.requireTestDatabaseExtension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.StandardTestDispatcher

class RealLocalScreenplayDataSourceTest : BehaviorSpec({
    extension(TestDatabaseExtension())

    Given("recommendations") {

        When("insert by id") {
            val recommendations = listOf(
                TmdbScreenplayIdSample.BreakingBad,
                TmdbScreenplayIdSample.Inception
            )
            val scenario = TestScenario()
            scenario.sut.insertRecommendedIds(recommendations)

            Then("recommendations are inserter") {
                coVerify { scenario.recommendationQueries.insert(DatabaseTmdbScreenplayIdSample.BreakingBad) }
                coVerify { scenario.recommendationQueries.insert(DatabaseTmdbScreenplayIdSample.Inception) }
            }
        }

        When("find by id") {
            val scenario = TestScenario()
            scenario.sut.findRecommendedIds()

            Then("finds suggestions on queries") {
                coVerify { scenario.recommendationQueries.findAll() }
            }
        }
    }
})

private class RealLocalScreenplayDataSourceTestScenario(
    val sut: RealLocalScreenplayDataSource,
    val recommendationQueries: RecommendationQueries
)

private fun Spec.TestScenario(): RealLocalScreenplayDataSourceTestScenario {
    val testDatabaseExtension = requireTestDatabaseExtension()
    testDatabaseExtension.clear()

    val database = testDatabaseExtension.database
    val recommendationQueries = spyk(database.recommendationQueries)

    return RealLocalScreenplayDataSourceTestScenario(
        sut = RealLocalScreenplayDataSource(
            readDispatcher = StandardTestDispatcher(),
            recommendationQueries = recommendationQueries,
            writeDispatcher = newSingleThreadContext("write")
        ),
        recommendationQueries = recommendationQueries
    )
}