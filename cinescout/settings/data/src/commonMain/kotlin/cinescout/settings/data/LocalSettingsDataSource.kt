package cinescout.settings.data

import cinescout.settings.domain.model.AppSettings
import cinescout.settings.domain.model.SuggestionSettings
import kotlinx.coroutines.flow.StateFlow

interface LocalSettingsDataSource {

    fun findAppSettings(): StateFlow<AppSettings>

    fun findSuggestionSettings(): StateFlow<SuggestionSettings>

    suspend fun updateAppSettings(newSettings: AppSettings)
}
