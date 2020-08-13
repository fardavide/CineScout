package network

import io.ktor.client.engine.apache.*

actual val ClientEngine = Apache.create()
