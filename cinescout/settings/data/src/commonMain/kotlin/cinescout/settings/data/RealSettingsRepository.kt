package cinescout.settings.data

import cinescout.settings.domain.SettingsRepository
import kotlinx.coroutines.flow.Flow

class RealSettingsRepository(
    private val localSettingsDataSource: LocalSettingsDataSource
) : SettingsRepository {

    override fun hasShownForYouHint(): Flow<Boolean> =
        localSettingsDataSource.hasShownForYouHint()

    override suspend fun setForYouHintShown() {
        localSettingsDataSource.setForYouHintShown()
    }
}
