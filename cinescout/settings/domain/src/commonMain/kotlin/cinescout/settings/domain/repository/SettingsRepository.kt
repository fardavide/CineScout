package cinescout.settings.domain.repository

import cinescout.settings.domain.model.AppSettings
import cinescout.settings.domain.model.SuggestionSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {

    fun getSuggestionSettings(): StateFlow<SuggestionSettings>

    suspend fun updateAppSettings(block: (AppSettings) -> AppSettings)

    suspend fun updateSuggestionSettings(block: (SuggestionSettings) -> SuggestionSettings) {
        updateAppSettings { appSettings ->
            appSettings.copy(suggestionSettings = block(appSettings.suggestionSettings))
        }
    }
}
