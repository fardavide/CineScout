package cinescout.sync.data.datasource

import cinescout.screenplay.domain.model.id.ScreenplayIds

interface LocalSyncDataSource {

    suspend fun findAllNotFetchedScreenplayIds(): List<ScreenplayIds>
}
