package cinescout.settings.domain.usecase

import cinescout.settings.domain.model.AppSettings
import cinescout.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.Factory

@Factory
class GetAppSettings(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): StateFlow<AppSettings> = settingsRepository.getAppSettings()
}
