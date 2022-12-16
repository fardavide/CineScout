package cinescout.network.testutil

import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*

operator fun MockEngine.plus(other: MockEngine): MockEngine =
    MockEngine(
        MockEngineConfig().apply {
            addHandler { request ->
                var response: HttpResponseData? = null
                var exception: Exception? = null
                for (handler in this@plus.config.requestHandlers + other.config.requestHandlers) {
                    try {
                        response = handler(this, request)
                        break
                    } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                        exception = e
                        continue
                    }
                }
                response
                    ?: throw exception!!
            }
        }
    )

fun MockEngine.setHandler(handler: MockRequestHandler) {
    val oldHandlers = config.requestHandlers + emptyList()
    config.requestHandlers.clear()
    config.addHandler(handler)
    config.requestHandlers.addAll(oldHandlers)
}
