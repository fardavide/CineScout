package cinescout.screenplay.data.remote.datasource

import arrow.core.right
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealRemoteScreenplayDataSourceTest : BehaviorSpec({

    Given("data source") {

        When("get recommended") {
            val recommended = listOf(
                ScreenplayIdsSample.BreakingBad,
                ScreenplayIdsSample.Inception
            )
            val scenario = TestScenario(recommended = recommended)

            Then("returns list of tmdb ids") {
                val result = scenario.sut.getRecommendedIds()
                result shouldBe recommended.right()
            }
        }
    }
})

private class RealRemoteScreenplayDataSourceTestScenario(
    val sut: RealRemoteScreenplayDataSource
)

private fun TestScenario(recommended: List<ScreenplayIds>): RealRemoteScreenplayDataSourceTestScenario {
    return RealRemoteScreenplayDataSourceTestScenario(
        sut = RealRemoteScreenplayDataSource(
            tmdbSource = FakeTmdbScreenplayRemoteDataSource(),
            traktSource = FakeTraktScreenplayRemoteDataSource(
                recommended = recommended
            )
        )
    )
}
