package cinescout.settings.data.local.mapper

import arrow.core.none
import cinescout.database.model.DatabaseAppSettings
import cinescout.settings.domain.model.AppSettings
import cinescout.settings.domain.model.SuggestionSettings
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseAppSettingsMapper {

    fun toDomainModel(
        @Suppress("UNUSED_PARAMETER") id: Long,
        anticipatedSuggestionsEnabled: Boolean,
        inAppSuggestionsEnabled: Boolean,
        personalSuggestionsEnabled: Boolean,
        popularSuggestionsEnabled: Boolean,
        recommendedSuggestionsEnabled: Boolean,
        trendingSuggestionsEnabled: Boolean
    ) = AppSettings(
        savedListOptions = none(), // TODO: Implement
        suggestionSettings = SuggestionSettings(
            isAnticipatedSuggestionsEnabled = anticipatedSuggestionsEnabled,
            isInAppGeneratedSuggestionsEnabled = inAppSuggestionsEnabled,
            isPersonalSuggestionsEnabled = personalSuggestionsEnabled,
            isPopularSuggestionsEnabled = popularSuggestionsEnabled,
            isRecommendedSuggestionsEnabled = recommendedSuggestionsEnabled,
            isTrendingSuggestionsEnabled = trendingSuggestionsEnabled
        )
    )
    
    fun toDomainModel(appSettings: DatabaseAppSettings): AppSettings = toDomainModel(
        id = appSettings.id,
        anticipatedSuggestionsEnabled = appSettings.anticipatedSuggestionsEnabled,
        inAppSuggestionsEnabled = appSettings.inAppSuggestionsEnabled,
        personalSuggestionsEnabled = appSettings.personalSuggestionsEnabled,
        popularSuggestionsEnabled = appSettings.popularSuggestionsEnabled,
        recommendedSuggestionsEnabled = appSettings.recommendedSuggestionsEnabled,
        trendingSuggestionsEnabled = appSettings.trendingSuggestionsEnabled
    )

    fun toDatabaseModel(appSettings: AppSettings) = DatabaseAppSettings(
        id = 1,
        anticipatedSuggestionsEnabled = appSettings.suggestionSettings.isAnticipatedSuggestionsEnabled,
        inAppSuggestionsEnabled = appSettings.suggestionSettings.isInAppGeneratedSuggestionsEnabled,
        personalSuggestionsEnabled = appSettings.suggestionSettings.isPersonalSuggestionsEnabled,
        popularSuggestionsEnabled = appSettings.suggestionSettings.isPopularSuggestionsEnabled,
        recommendedSuggestionsEnabled = appSettings.suggestionSettings.isRecommendedSuggestionsEnabled,
        trendingSuggestionsEnabled = appSettings.suggestionSettings.isTrendingSuggestionsEnabled
    )
}
