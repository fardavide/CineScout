package cinescout.lists.presentation.mapper

import cinescout.lists.presentation.sample.ListItemUiModelSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ListItemUiModelMapperTest : BehaviorSpec({

    Given("movie with personal rating") {
        val movieWithPersonalRating = ScreenplayWithPersonalRatingSample.Inception

        When("mapping to ui model") {
            val mapper = ListItemUiModelMapper()
            val result = mapper.toUiModel(movieWithPersonalRating)

            Then("should map to ui model") {
                result shouldBe ListItemUiModelSample.Inception
            }
        }

    }
})
