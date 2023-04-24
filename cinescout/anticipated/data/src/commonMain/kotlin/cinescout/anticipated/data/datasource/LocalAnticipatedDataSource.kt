package cinescout.anticipated.data.datasource

import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayType
import kotlinx.coroutines.flow.Flow

interface LocalAnticipatedDataSource {

    fun findMostAnticipated(type: ScreenplayType): Flow<List<Screenplay>>

    fun findMostAnticipatedIds(type: ScreenplayType): Flow<List<ScreenplayIds>>

    suspend fun insertMostAnticipated(screenplays: List<Screenplay>)

    suspend fun insertMostAnticipatedIds(ids: List<ScreenplayIds>)
}
