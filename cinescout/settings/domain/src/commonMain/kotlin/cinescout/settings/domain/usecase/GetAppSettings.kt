package cinescout.settings.domain.usecase

import cinescout.settings.domain.model.AppSettings
import cinescout.settings.domain.repository.SettingsRepository
import cinescout.settings.domain.sample.AppSettingsSample
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.Factory

interface GetAppSettings {

    operator fun invoke(): StateFlow<AppSettings>
}

@Factory
internal class RealGetAppSettings(
    private val settingsRepository: SettingsRepository
) : GetAppSettings {

    override operator fun invoke(): StateFlow<AppSettings> = settingsRepository.getAppSettings()
}

class FakeGetAppSettings(
    private val appSettings: AppSettings = AppSettingsSample.Default
) : GetAppSettings {

    override operator fun invoke(): StateFlow<AppSettings> = MutableStateFlow(appSettings)
}
