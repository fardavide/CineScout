package network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android

actual val ClientEngine: HttpClientEngine = Android.create()
