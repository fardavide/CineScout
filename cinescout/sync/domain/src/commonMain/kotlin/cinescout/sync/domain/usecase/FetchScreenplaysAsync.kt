package cinescout.sync.domain.usecase

import cinescout.CineScoutTestApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

interface FetchScreenplaysAsync {

    operator fun invoke()
}

@Factory
internal class RealFetchScreenplaysAsync(
    private val appScope: CoroutineScope,
    private val fetchScreenplays: FetchScreenplays
) : FetchScreenplaysAsync {

    override fun invoke() {
        appScope.launch { fetchScreenplays() }
    }
}

@CineScoutTestApi
class FakeFetchScreenplaysAsync : FetchScreenplaysAsync {

    override fun invoke() = Unit
}
