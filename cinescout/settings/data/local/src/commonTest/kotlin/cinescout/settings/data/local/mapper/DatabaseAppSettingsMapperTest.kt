package cinescout.settings.data.local.mapper

import cinescout.database.model.DatabaseAppSettings
import cinescout.settings.domain.model.SavedListOptions
import cinescout.settings.domain.model.SuggestionSettings
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class DatabaseAppSettingsMapperTest : BehaviorSpec({

    val mapper = DatabaseAppSettingsMapper()

    Given("a database app settings") {
        val databaseAppSettings = DatabaseAppSettings(
            id = 1,
            anticipatedSuggestionsEnabled = true,
            inAppSuggestionsEnabled = false,
            personalSuggestionsEnabled = true,
            popularSuggestionsEnabled = false,
            recommendedSuggestionsEnabled = true,
            trendingSuggestionsEnabled = false
        )

        When("mapping to domain model") {
            val domainModel = mapper.toDomainModel(databaseAppSettings)

            Then("the suggestion settings should be correct") {
                domainModel.suggestionSettings shouldBe SuggestionSettings(
                    isAnticipatedSuggestionsEnabled = true,
                    isInAppGeneratedSuggestionsEnabled = false,
                    isPersonalSuggestionsEnabled = true,
                    isPopularSuggestionsEnabled = false,
                    isRecommendedSuggestionsEnabled = true,
                    isTrendingSuggestionsEnabled = false
                )
            }

            Then("the saved list options should be none") {
                domainModel.savedListOptions shouldBe SavedListOptions(
                    filter = SavedListOptions.Filter.Liked,
                    sorting = SavedListOptions.Sorting.ReleaseDateAscending,
                    type = SavedListOptions.Type.Movies
                )
            }
        }
    }
})
