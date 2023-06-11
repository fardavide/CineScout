package cinescout.screenplay.data.datasource

import cinescout.CineScoutTestApi
import cinescout.notImplementedFake
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TraktScreenplayId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface LocalScreenplayDataSource {

    fun findRecommended(): Flow<List<Screenplay>>

    fun findRecommendedIds(): Flow<List<ScreenplayIds>>

    fun findScreenplay(ids: ScreenplayIds): Flow<Screenplay?> = findScreenplay(ids.trakt)

    fun findScreenplay(id: TraktScreenplayId): Flow<Screenplay?>

    fun findScreenplayGenres(id: TmdbScreenplayId): Flow<ScreenplayGenres?>

    fun findScreenplayKeywords(id: TmdbScreenplayId): Flow<ScreenplayKeywords?>

    fun findSimilar(ids: ScreenplayIds): Flow<List<Screenplay>>

    suspend fun insert(screenplays: List<Screenplay>)

    suspend fun insertRecommended(screenplays: List<Screenplay>)

    suspend fun insertRecommendedIds(ids: List<ScreenplayIds>)

    suspend fun insert(screenplay: Screenplay)

    suspend fun insertScreenplayGenres(screenplayGenres: ScreenplayGenres)

    suspend fun insertScreenplayKeywords(screenplayKeywords: ScreenplayKeywords)

    suspend fun insertSimilar(ids: ScreenplayIds, screenplays: List<Screenplay>) =
        insertSimilar(ids.tmdb, screenplays)

    suspend fun insertSimilar(id: TmdbScreenplayId, screenplays: List<Screenplay>)
}

@CineScoutTestApi
class FakeLocalScreenplayDataSource(
    recommended: List<Screenplay> = emptyList()
) : LocalScreenplayDataSource {

    private val mutableRecommended = MutableStateFlow(recommended)
    private val mutableRecommendedIds = MutableStateFlow(recommended.map { it.ids })

    override fun findRecommended(): Flow<List<Screenplay>> = mutableRecommended

    override fun findRecommendedIds(): Flow<List<ScreenplayIds>> = mutableRecommendedIds

    override fun findScreenplay(id: TraktScreenplayId): Flow<Screenplay?> {
        notImplementedFake()
    }

    override fun findScreenplayGenres(id: TmdbScreenplayId): Flow<ScreenplayGenres?> {
        notImplementedFake()
    }

    override fun findScreenplayKeywords(id: TmdbScreenplayId): Flow<ScreenplayKeywords?> {
        notImplementedFake()
    }

    override fun findSimilar(ids: ScreenplayIds): Flow<List<Screenplay>> {
        notImplementedFake()
    }

    override suspend fun insert(screenplays: List<Screenplay>) {
        notImplementedFake()
    }

    override suspend fun insert(screenplay: Screenplay) {
        notImplementedFake()
    }

    override suspend fun insertRecommended(screenplays: List<Screenplay>) {
        insertRecommendedIds(screenplays.map { it.ids })
        mutableRecommended.emit((mutableRecommended.value + screenplays).distinct())
    }

    override suspend fun insertRecommendedIds(ids: List<ScreenplayIds>) {
        mutableRecommendedIds.emit((mutableRecommendedIds.value + ids).distinct())
    }

    override suspend fun insertScreenplayGenres(screenplayGenres: ScreenplayGenres) {
        notImplementedFake()
    }

    override suspend fun insertScreenplayKeywords(screenplayKeywords: ScreenplayKeywords) {
        notImplementedFake()
    }

    override suspend fun insertSimilar(id: TmdbScreenplayId, screenplays: List<Screenplay>) {
        notImplementedFake()
    }
}
