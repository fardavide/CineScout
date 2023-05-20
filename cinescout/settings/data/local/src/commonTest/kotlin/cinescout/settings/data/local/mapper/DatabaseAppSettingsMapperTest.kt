package cinescout.settings.data.local.mapper

import arrow.core.some
import cinescout.database.model.DatabaseAppSettings
import cinescout.database.model.DatabaseListFilter
import cinescout.database.model.DatabaseListSorting
import cinescout.database.model.DatabaseListType
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
            trendingSuggestionsEnabled = false,

            savedListFilter = DatabaseListFilter.Liked,
            savedListSorting = DatabaseListSorting.ReleaseDateAscending,
            savedListType = DatabaseListType.Movies
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

            Then("the saved list options should be correct") {
                domainModel.savedListOptions shouldBe SavedListOptions(
                    filter = SavedListOptions.Filter.Liked,
                    sorting = SavedListOptions.Sorting.ReleaseDateAscending,
                    type = SavedListOptions.Type.Movies
                ).some()
            }
        }
    }

    Given("a domain app settings") {
        val domainAppSettings = cinescout.settings.domain.model.AppSettings(
            savedListOptions = SavedListOptions(
                filter = SavedListOptions.Filter.Liked,
                sorting = SavedListOptions.Sorting.ReleaseDateAscending,
                type = SavedListOptions.Type.Movies
            ).some(),
            suggestionSettings = SuggestionSettings(
                isAnticipatedSuggestionsEnabled = true,
                isInAppGeneratedSuggestionsEnabled = false,
                isPersonalSuggestionsEnabled = true,
                isPopularSuggestionsEnabled = false,
                isRecommendedSuggestionsEnabled = true,
                isTrendingSuggestionsEnabled = false
            )
        )

        When("mapping to database model") {
            val databaseModel = mapper.toDatabaseModel(domainAppSettings)

            Then("the suggestion settings should be correct") {
                databaseModel.anticipatedSuggestionsEnabled shouldBe true
                databaseModel.inAppSuggestionsEnabled shouldBe false
                databaseModel.personalSuggestionsEnabled shouldBe true
                databaseModel.popularSuggestionsEnabled shouldBe false
                databaseModel.recommendedSuggestionsEnabled shouldBe true
                databaseModel.trendingSuggestionsEnabled shouldBe false
            }

            Then("the saved list options should be none") {
                databaseModel.savedListFilter shouldBe DatabaseListFilter.Liked
                databaseModel.savedListSorting shouldBe DatabaseListSorting.ReleaseDateAscending
                databaseModel.savedListType shouldBe DatabaseListType.Movies
            }
        }
    }
})
