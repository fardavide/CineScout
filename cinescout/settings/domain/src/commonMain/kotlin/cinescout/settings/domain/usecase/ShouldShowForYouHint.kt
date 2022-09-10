package cinescout.settings.domain.usecase

import cinescout.settings.domain.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShouldShowForYouHint(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<Boolean> =
        settingsRepository.hasShownForYouHint()
            .map { hasShownForYouHint -> hasShownForYouHint.not() }
}
