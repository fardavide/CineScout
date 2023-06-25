package cinescout.screenplay.domain.repository

import cinescout.screenplay.domain.model.Genre
import kotlinx.coroutines.flow.Flow

interface ScreenplayCacheRepository {

    fun getAllKnownGenres(): Flow<List<Genre>>
}
