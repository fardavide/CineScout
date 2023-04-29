package cinescout.settings.data.local.mapper

import cinescout.database.model.DatabaseAppSettings
import cinescout.settings.domain.model.AppSettings
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
        isAnticipatedSuggestionsEnabled = anticipatedSuggestionsEnabled,
        isInAppGeneratedSuggestionsEnabled = inAppSuggestionsEnabled,
        isPersonalSuggestionsSuggestionsEnabled = personalSuggestionsEnabled,
        isPopularSuggestionsEnabled = popularSuggestionsEnabled,
        isRecommendedSuggestionsEnabled = recommendedSuggestionsEnabled,
        isTrendingSuggestionsEnabled = trendingSuggestionsEnabled
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
        anticipatedSuggestionsEnabled = appSettings.isAnticipatedSuggestionsEnabled,
        inAppSuggestionsEnabled = appSettings.isInAppGeneratedSuggestionsEnabled,
        personalSuggestionsEnabled = appSettings.isPersonalSuggestionsSuggestionsEnabled,
        popularSuggestionsEnabled = appSettings.isPopularSuggestionsEnabled,
        recommendedSuggestionsEnabled = appSettings.isRecommendedSuggestionsEnabled,
        trendingSuggestionsEnabled = appSettings.isTrendingSuggestionsEnabled
    )
}
