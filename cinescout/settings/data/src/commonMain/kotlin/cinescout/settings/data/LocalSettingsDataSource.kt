package cinescout.settings.data

import cinescout.settings.domain.model.AppSettings
import kotlinx.coroutines.flow.StateFlow

interface LocalSettingsDataSource {

    fun findAppSettings(): StateFlow<AppSettings>

    suspend fun updateAppSettings(newSettings: AppSettings)
}
