package cinescout.people.data

import cinescout.people.domain.model.ScreenplayCredits
import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.coroutines.flow.Flow

interface LocalPeopleDataSource {

    fun findCredits(screenplayId: TmdbScreenplayId): Flow<ScreenplayCredits>

    suspend fun insertCredits(credits: ScreenplayCredits)
}
