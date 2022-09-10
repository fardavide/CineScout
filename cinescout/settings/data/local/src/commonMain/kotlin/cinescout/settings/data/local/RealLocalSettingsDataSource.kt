package cinescout.settings.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneNotNull
import cinescout.database.AppSettings
import cinescout.database.AppSettingsQueries
import cinescout.database.util.suspendTransaction
import cinescout.settings.data.LocalSettingsDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class RealLocalSettingsDataSource(
    appScope: CoroutineScope,
    private val appSettingsQueries: AppSettingsQueries,
    private val databaseWriteDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher
) : LocalSettingsDataSource {

    private val appSettings: SharedFlow<AppSettings> =
        appSettingsQueries.find()
            .asFlow()
            .mapToOneNotNull(ioDispatcher)
            .shareIn(
                scope = appScope,
                started = SharingStarted.Eagerly,
                replay = 1
            )

    override fun hasShownForYouHint(): Flow<Boolean> =
        appSettings.map { settings -> settings.hasShownForYouHint }

    override suspend fun setForYouHintShown() {
        appSettingsQueries.suspendTransaction(databaseWriteDispatcher) {
            setHasShownForYouHint(true)
        }
    }
}
