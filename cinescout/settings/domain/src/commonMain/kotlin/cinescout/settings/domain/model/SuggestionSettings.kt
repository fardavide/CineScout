package cinescout.settings.domain.model

data class SuggestionSettings(
    val isAnticipatedSuggestionsEnabled: Boolean,
    val isInAppGeneratedSuggestionsEnabled: Boolean,
    val isPersonalSuggestionsEnabled: Boolean,
    val isPopularSuggestionsEnabled: Boolean,
    val isRecommendedSuggestionsEnabled: Boolean,
    val isTrendingSuggestionsEnabled: Boolean
)
