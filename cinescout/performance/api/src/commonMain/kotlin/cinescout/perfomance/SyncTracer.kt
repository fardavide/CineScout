package cinescout.perfomance

import arrow.core.Either
import cinescout.model.NetworkOperation
import org.koin.core.annotation.Factory

interface SyncTracer {

    suspend fun <T> disk(block: suspend () -> T): T

    suspend fun <T : Any> network(
        block: suspend () -> Either<NetworkOperation, T>
    ): Either<NetworkOperation, T>
}

interface SyncTracerFactory {

    fun create(name: String): SyncTracer
}

class RealSyncTracer internal constructor(
    private val name: String,
    private val performance: Performance
) : SyncTracer {

    override suspend fun <T> disk(block: suspend () -> T): T = performance.trace("$name-disk") { block() }

    override suspend fun <T : Any> network(
        block: suspend () -> Either<NetworkOperation, T>
    ): Either<NetworkOperation, T> = performance.trace("$name-network") { trace ->
        block().also { either ->
            trace["result"] = either.fold(
                ifLeft = { operation ->
                    when (operation) {
                        is NetworkOperation.Error -> "failed: ${operation.error}"
                        is NetworkOperation.Skipped -> "skipped"
                    }
                },
                ifRight = { "success" }
            )
        }
    }
}

@Factory
internal class RealSyncTracerFactory(
    private val performance: Performance
) : SyncTracerFactory {

    override fun create(name: String): SyncTracer = RealSyncTracer("Sync$name", performance)
}
