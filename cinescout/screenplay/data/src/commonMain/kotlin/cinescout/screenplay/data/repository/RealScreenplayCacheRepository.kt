package cinescout.screenplay.data.repository

import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.repository.ScreenplayCacheRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
internal class RealScreenplayCacheRepository(
    private val dataSource: LocalScreenplayDataSource
) : ScreenplayCacheRepository {

    override fun getAllKnownGenres(): Flow<List<Genre>> = dataSource.findAllGenres()
        .map { nullableList -> nullableList.orEmpty() }
}
