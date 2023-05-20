package cinescout.settings.data.local.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import arrow.core.Option
import cinescout.database.AppSettingsQueries
import cinescout.database.model.DefaultDatabaseAppSettings
import cinescout.database.util.suspendTransaction
import cinescout.settings.data.LocalSettingsDataSource
import cinescout.settings.data.local.mapper.DatabaseAppSettingsMapper
import cinescout.settings.domain.model.AppSettings
import cinescout.settings.domain.model.SavedListOptions
import cinescout.settings.domain.model.SuggestionSettings
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
internal class RealLocalSettingsDataSource(
    appScope: CoroutineScope,
    private val appSettingsQueries: AppSettingsQueries,
    @Named(IoDispatcher) ioDispatcher: CoroutineDispatcher,
    private val mapper: DatabaseAppSettingsMapper,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : LocalSettingsDataSource {

    internal val appSettings: StateFlow<AppSettings> =
        appSettingsQueries.find(mapper::toDomainModel)
            .asFlow()
            .mapToOneOrNull(ioDispatcher)
            .onEach { if (it == null) appSettingsQueries.insert(DefaultDatabaseAppSettings) }
            .filterNotNull()
            .stateIn(
                scope = appScope,
                started = SharingStarted.Eagerly,
                initialValue = mapper.toDomainModel(DefaultDatabaseAppSettings)
            )

    private val savedListOptions: StateFlow<Option<SavedListOptions>> =
        appSettings.map { it.savedListOptions }
            .stateIn(
                scope = appScope,
                started = SharingStarted.Eagerly,
                initialValue = appSettings.value.savedListOptions
            )

    private val suggestionSettings: StateFlow<SuggestionSettings> =
        appSettings.map { it.suggestionSettings }
            .stateIn(
                scope = appScope,
                started = SharingStarted.Eagerly,
                initialValue = appSettings.value.suggestionSettings
            )

    override fun findAppSettings(): StateFlow<AppSettings> = appSettings

    override fun findSavedListOptions(): StateFlow<Option<SavedListOptions>> = savedListOptions

    override fun findSuggestionSettings(): StateFlow<SuggestionSettings> = suggestionSettings

    override suspend fun updateAppSettings(newSettings: AppSettings) {
        appSettingsQueries.suspendTransaction(writeDispatcher) {
            appSettingsQueries.insert(mapper.toDatabaseModel(newSettings))
        }
    }
}
