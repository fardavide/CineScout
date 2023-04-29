package cinescout.settings.domain.repository

import cinescout.settings.domain.model.AppSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {

    fun getAppSettings(): StateFlow<AppSettings>

    suspend fun updateAppSettings(block: (AppSettings) -> AppSettings)
}
