package cinescout.settings.domain.sample

import cinescout.settings.domain.model.AppSettings

object AppSettingsSample {

    val Default = AppSettings(
        isAnticipatedSuggestionsEnabled = true,
        isInAppGeneratedSuggestionsEnabled = true,
        isPersonalSuggestionsEnabled = true,
        isPopularSuggestionsEnabled = true,
        isRecommendedSuggestionsEnabled = true,
        isTrendingSuggestionsEnabled = true
    )
}
