package cinescout.settings.data

import kotlinx.coroutines.flow.Flow

interface LocalSettingsDataSource {

    fun hasShownForYouHint(): Flow<Boolean>

    suspend fun setForYouHintShown()
}
