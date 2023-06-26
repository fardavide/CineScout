package cinescout.screenplay.data.local.datasource

import cinescout.database.RecommendationQueries
import cinescout.database.sample.DatabaseTmdbScreenplayIdSample
import cinescout.database.sample.DatabaseTraktScreenplayIdSample
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import cinescout.test.database.TestDatabaseExtension
import cinescout.test.database.requireTestDatabaseExtension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.StandardTestDispatcher

class RealLocalScreenplayDataSourceTest : BehaviorSpec({
    extension(TestDatabaseExtension())

    Given("recommendations") {

        When("insert by id") {
            val recommendations = listOf(
                ScreenplayIdsSample.BreakingBad,
                ScreenplayIdsSample.Inception
            )
            val scenario = TestScenario()
            scenario.sut.insertRecommendedIds(recommendations)

            Then("recommendations are inserter") {
                coVerify {
                    scenario.recommendationQueries.insert(
                        DatabaseTraktScreenplayIdSample.BreakingBad,
                        DatabaseTmdbScreenplayIdSample.BreakingBad
                    )
                }
                coVerify {
                    scenario.recommendationQueries.insert(
                        DatabaseTraktScreenplayIdSample.Inception,
                        DatabaseTmdbScreenplayIdSample.Inception
                    )
                }
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
            databaseScreenplayMapper = mockk(),
            genreQueries = mockk(),
            genreMapper = mockk(),
            keywordQueries = mockk(),
            movieQueries = mockk(),
            readDispatcher = StandardTestDispatcher(),
            recommendationQueries = recommendationQueries,
            screenplayGenreQueries = mockk(),
            screenplayKeywordQueries = mockk(),
            screenplayQueries = mockk(),
            similarQueries = mockk(),
            transacter = mockk(),
            tvShowQueries = mockk(),
            writeDispatcher = newSingleThreadContext("write")
        ),
        recommendationQueries = recommendationQueries
    )
}
