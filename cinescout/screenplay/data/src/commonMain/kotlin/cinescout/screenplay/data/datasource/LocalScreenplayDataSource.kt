package cinescout.screenplay.data.datasource

import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface LocalScreenplayDataSource {

    fun findRecommended(): Flow<List<Screenplay>>

    fun findRecommendedIds(): Flow<List<TmdbScreenplayId>>

    fun findScreenplay(id: TmdbScreenplayId): Flow<Screenplay?>

    fun findScreenplayGenres(id: TmdbScreenplayId): Flow<ScreenplayGenres?>

    fun findSimilar(id: TmdbScreenplayId): Flow<List<Screenplay>>

    suspend fun insert(screenplays: List<Screenplay>)

    suspend fun insertRecommended(screenplays: List<Screenplay>)

    suspend fun insertRecommendedIds(ids: List<TmdbScreenplayId>)

    suspend fun insert(screenplay: Screenplay)

    suspend fun insertScreenplayGenres(screenplayGenres: ScreenplayGenres)

    suspend fun insertSimilar(id: TmdbScreenplayId, screenplays: List<Screenplay>)
}

class FakeLocalScreenplayDataSource(
    recommended: List<Screenplay> = emptyList()
) : LocalScreenplayDataSource {

    private val mutableRecommended = MutableStateFlow(recommended)
    private val mutableRecommendedIds = MutableStateFlow(recommended.map { it.tmdbId })

    override fun findRecommended(): Flow<List<Screenplay>> = mutableRecommended

    override fun findRecommendedIds(): Flow<List<TmdbScreenplayId>> = mutableRecommendedIds
    override fun findScreenplay(id: TmdbScreenplayId): Flow<Screenplay?> {
        TODO("Not yet implemented")
    }

    override fun findScreenplayGenres(id: TmdbScreenplayId): Flow<ScreenplayGenres?> {
        TODO("Not yet implemented")
    }

    override fun findSimilar(id: TmdbScreenplayId): Flow<List<Screenplay>> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(screenplays: List<Screenplay>) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(screenplay: Screenplay) {
        TODO("Not yet implemented")
    }

    override suspend fun insertRecommended(screenplays: List<Screenplay>) {
        insertRecommendedIds(screenplays.map { it.tmdbId })
        mutableRecommended.emit((mutableRecommended.value + screenplays).distinct())
    }

    override suspend fun insertRecommendedIds(ids: List<TmdbScreenplayId>) {
        mutableRecommendedIds.emit((mutableRecommendedIds.value + ids).distinct())
    }

    override suspend fun insertScreenplayGenres(screenplayGenres: ScreenplayGenres) {
        TODO("Not yet implemented")
    }

    override suspend fun insertSimilar(id: TmdbScreenplayId, screenplays: List<Screenplay>) {
        TODO("Not yet implemented")
    }
}
