package cinescout.settings.data

import cinescout.settings.domain.model.AppSettings
import cinescout.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.Factory

@Factory
internal class RealSettingsRepository(
    private val localSettingsDataSource: LocalSettingsDataSource
) : SettingsRepository {

    override fun getAppSettings(): StateFlow<AppSettings> = localSettingsDataSource.findAppSettings()

    override suspend fun updateAppSettings(block: (AppSettings) -> AppSettings) {
        val currentSettings = localSettingsDataSource.findAppSettings().value
        val newSettings = block(currentSettings)
        localSettingsDataSource.updateAppSettings(newSettings)
    }
}
