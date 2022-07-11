package cinescout.network.testutil

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.request.HttpResponseData

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
