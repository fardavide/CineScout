package cinescout.settings.domain.usecase

import cinescout.settings.domain.model.SuggestionSettings
import cinescout.settings.domain.repository.SettingsRepository
import org.koin.core.annotation.Factory

@Factory
class UpdateSuggestionSettings(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(block: (SuggestionSettings) -> SuggestionSettings) {
        settingsRepository.updateSuggestionSettings(block)
    }
}
