package cinescout.settings.domain.model

data class AppSettings(
    val isAnticipatedSuggestionsEnabled: Boolean,
    val isInAppGeneratedSuggestionsEnabled: Boolean,
    val isPersonalSuggestionsSuggestionsEnabled: Boolean,
    val isPopularSuggestionsEnabled: Boolean,
    val isRecommendedSuggestionsEnabled: Boolean,
    val isTrendingSuggestionsEnabled: Boolean
)
