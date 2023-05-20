package cinescout.settings.domain.usecase

import cinescout.settings.domain.model.SuggestionSettings
import cinescout.settings.domain.repository.SettingsRepository
import cinescout.settings.domain.sample.SuggestionSettingsSample
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.Factory

interface GetSuggestionSettings {

    operator fun invoke(): StateFlow<SuggestionSettings>
}

@Factory
internal class RealGetSuggestionSettings(
    private val settingsRepository: SettingsRepository
) : GetSuggestionSettings {

    override operator fun invoke(): StateFlow<SuggestionSettings> = settingsRepository.getSuggestionSettings()
}

class FakeGetSuggestionSettings(
    private val suggestionSettings: SuggestionSettings = SuggestionSettingsSample.Default
) : GetSuggestionSettings {

    override operator fun invoke(): StateFlow<SuggestionSettings> = MutableStateFlow(suggestionSettings)
}
