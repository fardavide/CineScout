package cinescout.sync.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.fetchdata.domain.repository.FetchDataRepository
import org.koin.core.annotation.Factory

interface ClearFetchData {

    suspend operator fun invoke()
}

@Factory
internal class RealClearFetchData(
    private val fetchDataRepository: FetchDataRepository
) : ClearFetchData {

    override suspend fun invoke() {
        fetchDataRepository.clear()
    }
}

@CineScoutTestApi
class FakeClearFetchData : ClearFetchData {

    override suspend fun invoke() = Unit
}
