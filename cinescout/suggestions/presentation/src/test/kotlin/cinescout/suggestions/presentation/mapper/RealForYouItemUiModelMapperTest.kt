package cinescout.suggestions.presentation.mapper

import cinescout.suggestions.domain.sample.SuggestedScreenplayWithExtrasSample
import cinescout.suggestions.presentation.sample.ForYouScreenplayUiModelSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealForYouItemUiModelMapperTest : BehaviorSpec({

    val mapper = RealForYouItemUiModelMapper()

    Given("a movie") {
        val movie = SuggestedScreenplayWithExtrasSample.Inception
        val expected = ForYouScreenplayUiModelSample.Inception

        When("mapping to ui model") {
            val result = mapper.toUiModel(movie)

            Then("it should map to ui model") {
                result shouldBe expected
            }
        }
    }

    Given("a tv show") {
        val tvShow = SuggestedScreenplayWithExtrasSample.BreakingBad
        val expected = ForYouScreenplayUiModelSample.BreakingBad

        When("mapping to ui model") {
            val result = mapper.toUiModel(tvShow)

            Then("it should map to ui model") {
                result shouldBe expected
            }
        }
    }
})
