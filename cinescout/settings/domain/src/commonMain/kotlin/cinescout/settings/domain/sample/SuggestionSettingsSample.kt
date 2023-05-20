package cinescout.settings.domain.sample

import cinescout.settings.domain.model.SuggestionSettings

object SuggestionSettingsSample {

    val Default = SuggestionSettings(
        isAnticipatedSuggestionsEnabled = true,
        isInAppGeneratedSuggestionsEnabled = true,
        isPersonalSuggestionsEnabled = true,
        isPopularSuggestionsEnabled = true,
        isRecommendedSuggestionsEnabled = true,
        isTrendingSuggestionsEnabled = true
    )
}
