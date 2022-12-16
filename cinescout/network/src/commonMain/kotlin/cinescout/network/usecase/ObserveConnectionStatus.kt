package cinescout.network.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.model.ConnectionStatus
import cinescout.utils.kotlin.DispatcherQualifier
import cinescout.utils.kotlin.ticker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.seconds

@Single
class ObserveConnectionStatus internal constructor(
    appScope: CoroutineScope,
    @Named(DispatcherQualifier.Io) private val ioDispatcher: CoroutineDispatcher,
    private val ping: Ping
) {

    internal val flow: Flow<ConnectionStatus>
        get() = ticker(30.seconds) {
            val status = coroutineScope {
                val device = async { ping(Ping.Host.Google) }
                val tmdb = async { ping(Ping.Host.Tmdb) }
                val trakt = async { ping(Ping.Host.Trakt) }
                ConnectionStatus(
                    device = device.await().toConnectionStatus(),
                    tmdb = tmdb.await().toConnectionStatus(),
                    trakt = trakt.await().toConnectionStatus()
                )
            }
            emit(status)
        }.distinctUntilChanged()

    private val sharedFlow: SharedFlow<ConnectionStatus> = flow
        .flowOn(ioDispatcher)
        .shareIn(
            scope = appScope,
            started = SharingStarted.Eagerly,
            replay = 1
        )

    operator fun invoke(): Flow<ConnectionStatus> =
        sharedFlow

    private fun Either<NetworkError, Unit>.toConnectionStatus() =
        fold(
            ifLeft = { ConnectionStatus.Connection.Offline },
            ifRight = { ConnectionStatus.Connection.Online }
        )
}
