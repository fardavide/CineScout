package client.android

import client.DispatchersProvider
import client.clientModule
import co.touchlab.kermit.LogcatLogger
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import org.koin.dsl.module

val androidClientModule = module {

    factory<Logger> { LogcatLogger() }

    // TODO!!!
    factory<CoroutineScope> { GlobalScope }

    factory<DispatchersProvider> {
        object : DispatchersProvider {
            override val Comp = Dispatchers.Default
            override val Io = Dispatchers.IO
            override val Main: CoroutineDispatcher = Dispatchers.Main
        }
    }

} + clientModule
