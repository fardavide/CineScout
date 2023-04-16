package cinescout.screenplay.data.datasource

import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface LocalScreenplayDataSource {

    fun findRecommended(): Flow<List<Screenplay>>

    fun findRecommendedIds(): Flow<List<ScreenplayIds>>

    fun findScreenplay(id: TraktScreenplayId): Flow<Screenplay?>

    fun findScreenplayGenres(id: TmdbScreenplayId): Flow<ScreenplayGenres?>

    fun findScreenplayKeywords(id: TmdbScreenplayId): Flow<ScreenplayKeywords?>

    fun findSimilar(id: TmdbScreenplayId): Flow<List<Screenplay>>

    suspend fun insert(screenplays: List<Screenplay>)

    suspend fun insertRecommended(screenplays: List<Screenplay>)

    suspend fun insertRecommendedIds(ids: List<ScreenplayIds>)

    suspend fun insert(screenplay: Screenplay)

    suspend fun insertScreenplayGenres(screenplayGenres: ScreenplayGenres)

    suspend fun insertScreenplayKeywords(screenplayKeywords: ScreenplayKeywords)

    suspend fun insertSimilar(id: TmdbScreenplayId, screenplays: List<Screenplay>)
}

class FakeLocalScreenplayDataSource(
    recommended: List<Screenplay> = emptyList()
) : LocalScreenplayDataSource {

    private val mutableRecommended = MutableStateFlow(recommended)
    private val mutableRecommendedIds = MutableStateFlow(recommended.map { it.ids })

    override fun findRecommended(): Flow<List<Screenplay>> = mutableRecommended

    override fun findRecommendedIds(): Flow<List<ScreenplayIds>> = mutableRecommendedIds

    override fun findScreenplay(id: TraktScreenplayId): Flow<Screenplay?> {
        TODO("Not yet implemented")
    }

    override fun findScreenplayGenres(id: TmdbScreenplayId): Flow<ScreenplayGenres?> {
        TODO("Not yet implemented")
    }

    override fun findScreenplayKeywords(id: TmdbScreenplayId): Flow<ScreenplayKeywords?> {
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
        insertRecommendedIds(screenplays.map { it.ids })
        mutableRecommended.emit((mutableRecommended.value + screenplays).distinct())
    }

    override suspend fun insertRecommendedIds(ids: List<ScreenplayIds>) {
        mutableRecommendedIds.emit((mutableRecommendedIds.value + ids).distinct())
    }

    override suspend fun insertScreenplayGenres(screenplayGenres: ScreenplayGenres) {
        TODO("Not yet implemented")
    }

    override suspend fun insertScreenplayKeywords(screenplayKeywords: ScreenplayKeywords) {
        TODO("Not yet implemented")
    }

    override suspend fun insertSimilar(id: TmdbScreenplayId, screenplays: List<Screenplay>) {
        TODO("Not yet implemented")
    }
}
