package cinescout.settings.data

import cinescout.settings.domain.SettingsRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class RealSettingsRepository(
    private val localSettingsDataSource: LocalSettingsDataSource
) : SettingsRepository {

    override fun hasShownForYouHint(): Flow<Boolean> =
        localSettingsDataSource.hasShownForYouHint()

    override suspend fun setForYouHintShown() {
        localSettingsDataSource.setForYouHintShown()
    }
}
