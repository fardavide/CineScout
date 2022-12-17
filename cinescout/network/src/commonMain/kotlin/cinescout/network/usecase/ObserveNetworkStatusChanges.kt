package cinescout.network.usecase

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
expect class ObserveNetworkStatusChanges {

    operator fun invoke(): Flow<Unit>
}
