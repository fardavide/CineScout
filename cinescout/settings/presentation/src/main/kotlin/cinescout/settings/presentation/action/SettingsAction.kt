package cinescout.settings.presentation.action

sealed interface SettingsAction {

    data class UpdateAnticipated(val value: Boolean) : SettingsAction
    data class UpdateInAppGenerated(val value: Boolean) : SettingsAction
    data class UpdatePersonalSuggestions(val value: Boolean) : SettingsAction
    data class UpdatePopular(val value: Boolean) : SettingsAction
    data class UpdateRecommended(val value: Boolean) : SettingsAction
    data class UpdateTrending(val value: Boolean) : SettingsAction
}
