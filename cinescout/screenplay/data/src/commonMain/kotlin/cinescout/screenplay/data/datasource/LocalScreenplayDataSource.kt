package cinescout.screenplay.data.datasource

import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface LocalScreenplayDataSource {

    fun findRecommended(): Flow<List<Screenplay>>

    fun findRecommendedIds(): Flow<List<TmdbScreenplayId>>

    suspend fun insertRecommended(screenplays: List<Screenplay>)

    suspend fun insertRecommendedIds(ids: List<TmdbScreenplayId>)
}

class FakeLocalScreenplayDataSource(
    recommended: List<Screenplay> = emptyList()
) : LocalScreenplayDataSource {

    private val mutableRecommended = MutableStateFlow(recommended)
    private val mutableRecommendedIds = MutableStateFlow(recommended.map { it.tmdbId })

    override fun findRecommended(): Flow<List<Screenplay>> = mutableRecommended

    override fun findRecommendedIds(): Flow<List<TmdbScreenplayId>> = mutableRecommendedIds

    override suspend fun insertRecommended(screenplays: List<Screenplay>) {
        insertRecommendedIds(screenplays.map { it.tmdbId })
        mutableRecommended.emit((mutableRecommended.value + screenplays).distinct())
    }

    override suspend fun insertRecommendedIds(ids: List<TmdbScreenplayId>) {
        mutableRecommendedIds.emit((mutableRecommendedIds.value + ids).distinct())
    }
}