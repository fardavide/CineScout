package cinescout.lists.presentation.mapper

import cinescout.lists.presentation.previewdata.ListItemUiModelPreviewData
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ListItemUiModelMapperTest : BehaviorSpec({

    Given("movie with personal rating") {
        val movieWithPersonalRating = MovieWithPersonalRatingSample.Inception

        When("mapping to ui model") {
            val mapper = ListItemUiModelMapper()
            val result = mapper.toUiModel(movieWithPersonalRating)

            Then("should map to ui model") {
                result shouldBe ListItemUiModelPreviewData.Inception
            }
        }

    }
})
