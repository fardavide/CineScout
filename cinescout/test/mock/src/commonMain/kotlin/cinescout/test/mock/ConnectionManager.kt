package cinescout.test.mock

import cinescout.network.model.ConnectionStatus
import cinescout.network.usecase.ObserveConnectionStatus
import kotlinx.coroutines.flow.flowOf
import org.koin.core.component.KoinComponent
import org.koin.dsl.module

object ConnectionManager : KoinComponent {

    fun setConnection(status: ConnectionStatus) {
        val connectionModule = module {
            single { createObserveConnectionStatus(status) }
        }
        getKoin().loadModules(listOf(connectionModule))
    }

    private fun createObserveConnectionStatus(status: ConnectionStatus): ObserveConnectionStatus =
        object : ObserveConnectionStatus {
            override fun invoke() = flowOf(status)
        }
}
