package cinescout.settings.domain.usecase

import cinescout.settings.domain.SettingsRepository
import org.koin.core.annotation.Factory

@Factory
class SetForYouHintShown(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke() {
        settingsRepository.setForYouHintShown()
    }
}
