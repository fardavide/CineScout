package cinescout.network.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.model.ConnectionStatus
import cinescout.utils.kotlin.IoDispatcher
import cinescout.utils.kotlin.ticker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.seconds

interface ObserveConnectionStatus {

    operator fun invoke(): Flow<ConnectionStatus>

    companion object {

        val DefaultInterval = 30.seconds
    }
}

@Single
internal class RealObserveConnectionStatus internal constructor(
    appScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    observeNetworkStatusChanges: ObserveNetworkStatusChanges,
    private val ping: Ping
) : ObserveConnectionStatus {

    private val flow: Flow<ConnectionStatus> =
        combine(
            observeNetworkStatusChanges(),
            ticker(ObserveConnectionStatus.DefaultInterval) { emit(Unit) }
        ) { _, _ ->
            coroutineScope {
                val device = async { ping(Ping.Host.Google) }
                val tmdb = async { ping(Ping.Host.Tmdb) }
                val trakt = async { ping(Ping.Host.Trakt) }
                ConnectionStatus(
                    device = device.await().toConnectionStatus(),
                    tmdb = tmdb.await().toConnectionStatus(),
                    trakt = trakt.await().toConnectionStatus()
                )
            }
        }.distinctUntilChanged()

    private val sharedFlow: SharedFlow<ConnectionStatus> = flow
        .flowOn(ioDispatcher)
        .shareIn(
            scope = appScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1
        )

    override operator fun invoke(): Flow<ConnectionStatus> = sharedFlow

    private fun Either<NetworkError, Unit>.toConnectionStatus() = fold(
        ifLeft = { error ->
            when (error) {
                NetworkError.Forbidden,
                NetworkError.Unauthorized -> ConnectionStatus.Connection.Online
                else -> ConnectionStatus.Connection.Offline
            }
        },
        ifRight = { ConnectionStatus.Connection.Online }
    )
}

class FakeObserveConnectionStatus(
    private val connectionStatus: ConnectionStatus = ConnectionStatus.AllOnline
) : ObserveConnectionStatus {

    override operator fun invoke(): Flow<ConnectionStatus> = flowOf(connectionStatus)
}
