package cinescout.screenplay.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.repository.ScreenplayCacheRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetAllKnownGenres {

    operator fun invoke(): Flow<List<Genre>>
}

@Factory
internal class RealGetAllKnownGenres(
    private val screenplayCacheRepository: ScreenplayCacheRepository
) : GetAllKnownGenres {

    override fun invoke(): Flow<List<Genre>> = screenplayCacheRepository.getAllKnownGenres()
}

@CineScoutTestApi
class FakeGetAllKnownGenres : GetAllKnownGenres {

    override fun invoke(): Flow<List<Genre>> = flowOf(emptyList())
}
