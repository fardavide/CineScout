package cinescout.voting.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.screenplay.domain.store.ScreenplayStore
import cinescout.voting.domain.repository.VotedScreenplayRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

interface FetchVotedScreenplaysIfNeeded {

    operator fun invoke()
}

@Factory
internal class RealFetchVotedScreenplaysIfNeeded(
    private val appScope: CoroutineScope,
    private val repository: VotedScreenplayRepository,
    private val screenplayStore: ScreenplayStore
) : FetchVotedScreenplaysIfNeeded {

    override operator fun invoke() {
        appScope.launch {
            for (id in repository.getAllVotedIds().first()) {
                screenplayStore.getCached(id, refresh = false)
            }
        }
    }
}

@CineScoutTestApi
class FakeFetchVotedScreenplaysIfNeeded : FetchVotedScreenplaysIfNeeded {

    override fun invoke() {}
}
