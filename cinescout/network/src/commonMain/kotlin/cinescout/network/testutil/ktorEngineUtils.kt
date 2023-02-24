package cinescout.network.testutil

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData

operator fun MockEngine.plus(other: MockEngine): MockEngine = MockEngine(
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

fun MockEngine.addHandler(handler: MockRequestHandler) {
    val supervisor = config.requestHandlers.filterIsInstance<SupervisorMockRequestHandler>().firstOrNull()
    if (supervisor != null) {
        supervisor.children += handler
    } else {
        val all = listOf(handler) + config.requestHandlers
        config.requestHandlers.clear()
        config.addHandler(SupervisorMockRequestHandler(all.toMutableList()))
    }
}

private class SupervisorMockRequestHandler(
    val children: MutableList<MockRequestHandler>
) : MockRequestHandler {

    override suspend fun invoke(
        requestHandleScope: MockRequestHandleScope,
        request: HttpRequestData
    ): HttpResponseData {
        var response: HttpResponseData? = null
        var exception: Exception? = null
        for (child in children) {
            try {
                response = child(requestHandleScope, request)
                break
            } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                exception = e
                continue
            }
        }
        return response
            ?: throw exception!!
    }
}
