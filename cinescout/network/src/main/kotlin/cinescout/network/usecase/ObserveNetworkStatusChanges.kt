package cinescout.network.usecase

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Single
actual class ObserveNetworkStatusChanges : KoinComponent {

    private val appScope: CoroutineScope by inject()
    private val connectivityManager: ConnectivityManager by inject()

    private val flow = callbackFlow {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        val networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(Unit)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                trySend(Unit)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(Unit)
            }
        }
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        awaitClose { connectivityManager.unregisterNetworkCallback(networkCallback) }
    }

    private val sharedFlow = flow.shareIn(
        scope = appScope,
        started = SharingStarted.Eagerly,
        replay = 1
    )

    actual operator fun invoke(): Flow<Unit> =
        sharedFlow
}
