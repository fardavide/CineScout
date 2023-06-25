package cinescout.screenplay.data.repository

import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.domain.repository.ScreenplayCacheRepository
import org.koin.core.annotation.Factory

@Factory
internal class RealScreenplayCacheRepository(
    private val dataSource: LocalScreenplayDataSource
) : ScreenplayCacheRepository {

    override fun getAllKnownGenres() = dataSource.findAllGenres()
}
