package cinescout.people.data.datasource

import cinescout.people.domain.model.ScreenplayCredits
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface LocalPeopleDataSource {

    fun findCredits(screenplayId: TmdbScreenplayId): Flow<ScreenplayCredits>

    suspend fun insertCredits(credits: ScreenplayCredits)
}

class FakeLocalPeopleDataSource(
    private val credits: ScreenplayCredits? = null
) : LocalPeopleDataSource {

    private val mutableCredits = MutableStateFlow(
        credits?.let { mutableMapOf(credits.screenplayId to credits) } ?: mutableMapOf()
    )

    override fun findCredits(screenplayId: TmdbScreenplayId): Flow<ScreenplayCredits> =
        mutableCredits.map { map ->
            map[screenplayId]
                ?: ScreenplayCredits(screenplayId, cast = emptyList(), crew = emptyList())
        }

    override suspend fun insertCredits(credits: ScreenplayCredits) {
        mutableCredits.emit(mutableCredits.value.apply { put(credits.screenplayId, credits) })
    }
}
