package cinescout.settings.presentation.model

internal data class SettingsUiModel(
    val suggestions: Suggestions
) {

    data class Suggestions(
        val isAnticipatedEnabled: Boolean,
        val isInAppGeneratedEnabled: Boolean,
        val isPersonalSuggestionsEnabled: Boolean,
        val isPopularEnabled: Boolean,
        val isRecommendedEnabled: Boolean,
        val isTrendingEnabled: Boolean
    )
}
