package cinescout.network.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Single

interface ObserveNetworkStatusChanges {

    operator fun invoke(): Flow<Unit>
}

@Single
expect class RealObserveNetworkStatusChanges : ObserveNetworkStatusChanges {

    override operator fun invoke(): Flow<Unit>
}

class FakeObserveNetworkStatusChanges(
    private val flow: Flow<Unit> = flowOf(Unit)
) : ObserveNetworkStatusChanges {

    override operator fun invoke(): Flow<Unit> = flow
}
