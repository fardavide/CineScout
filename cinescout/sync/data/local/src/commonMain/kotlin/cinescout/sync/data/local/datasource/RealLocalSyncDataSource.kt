package cinescout.sync.data.local.datasource

import cinescout.database.SyncQueries
import cinescout.screenplay.data.local.mapper.toScreenplayIds
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.sync.data.datasource.LocalSyncDataSource
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalSyncDataSource(
    @Named(IoDispatcher) private val dispatcher: CoroutineDispatcher,
    private val syncQueries: SyncQueries
) : LocalSyncDataSource {

    override suspend fun findAllNotFetchedScreenplayIds(): List<ScreenplayIds> = withContext(dispatcher) {
        syncQueries.findAllNotFetchedScreenplayIds(::toScreenplayIds).executeAsList()
    }
}
