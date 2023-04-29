package cinescout.settings.domain.usecase

import cinescout.settings.domain.model.AppSettings
import cinescout.settings.domain.repository.SettingsRepository
import org.koin.core.annotation.Factory

@Factory
class UpdateAppSettings(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(block: (AppSettings) -> AppSettings) {
        settingsRepository.updateAppSettings(block)
    }
}
