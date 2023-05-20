package cinescout.settings.data

import arrow.core.Option
import cinescout.settings.domain.model.AppSettings
import cinescout.settings.domain.model.SavedListOptions
import cinescout.settings.domain.model.SuggestionSettings
import kotlinx.coroutines.flow.StateFlow

interface LocalSettingsDataSource {

    fun findAppSettings(): StateFlow<AppSettings>

    fun findSavedListOptions(): StateFlow<Option<SavedListOptions>>

    fun findSuggestionSettings(): StateFlow<SuggestionSettings>

    suspend fun updateAppSettings(newSettings: AppSettings)
}
