package cinescout.settings.domain

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun hasShownForYouHint(): Flow<Boolean>

    suspend fun setForYouHintShown()
}
