package cinescout.settings.domain.usecase

import cinescout.settings.domain.SettingsRepository

class SetForYouHintShown(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke() {
        settingsRepository.setForYouHintShown()
    }
}
