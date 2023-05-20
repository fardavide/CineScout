package cinescout.settings.domain.repository

import arrow.core.Option
import arrow.core.some
import cinescout.settings.domain.model.AppSettings
import cinescout.settings.domain.model.SavedListOptions
import cinescout.settings.domain.model.SuggestionSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {

    fun getSavedListOptions(): StateFlow<Option<SavedListOptions>>

    fun getSuggestionSettings(): StateFlow<SuggestionSettings>

    suspend fun updateAppSettings(block: (AppSettings) -> AppSettings)

    suspend fun updateSavedListOptions(savedListOptions: SavedListOptions) {
        updateAppSettings { appSettings ->
            appSettings.copy(savedListOptions = savedListOptions.some())
        }
    }

    suspend fun updateSuggestionSettings(block: (SuggestionSettings) -> SuggestionSettings) {
        updateAppSettings { appSettings ->
            appSettings.copy(suggestionSettings = block(appSettings.suggestionSettings))
        }
    }
}
