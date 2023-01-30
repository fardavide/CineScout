package cinescout.settings.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneNotNull
import cinescout.database.AppSettings
import cinescout.database.AppSettingsQueries
import cinescout.settings.data.LocalSettingsDataSource
import cinescout.utils.kotlin.DispatcherQualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class RealLocalSettingsDataSource(
    appScope: CoroutineScope,
    appSettingsQueries: AppSettingsQueries,
    @Named(DispatcherQualifier.Io) ioDispatcher: CoroutineDispatcher
) : LocalSettingsDataSource {

    internal val appSettings: SharedFlow<AppSettings> =
        appSettingsQueries.find()
            .asFlow()
            .mapToOneNotNull(ioDispatcher)
            .map(::AppSettings)
            .shareIn(
                scope = appScope,
                started = SharingStarted.Eagerly,
                replay = 1
            )

}
