package cinescout.screenplay.data.remote.datasource

import arrow.core.right
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealRemoteScreenplayDataSourceTest : BehaviorSpec({

    Given("data source") {

        When("get recommended") {
            val recommended = listOf(
                TmdbScreenplayIdSample.BreakingBad,
                TmdbScreenplayIdSample.Inception
            )
            val scenario = TestScenario(recommended = recommended)

            Then("returns list of tmdb ids") {
                val result = scenario.sut.getRecommended()
                result shouldBe recommended.right()
            }
        }
    }
})

private class RealRemoteScreenplayDataSourceTestScenario(
    val sut: RealRemoteScreenplayDataSource
)

private fun TestScenario(recommended: List<TmdbScreenplayId>): RealRemoteScreenplayDataSourceTestScenario {
    return RealRemoteScreenplayDataSourceTestScenario(
        sut = RealRemoteScreenplayDataSource(
            traktSource = FakeTraktScreenplayRemoteDataSource(
                recommended = recommended
            )
        )
    )
}
