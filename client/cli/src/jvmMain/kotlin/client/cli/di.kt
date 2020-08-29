package client.cli

import client.DispatchersProvider
import client.clientModule
import co.touchlab.kermit.CommonLogger
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val cliClientModule = module {

    factory { CoroutineScope(Job()) }
    factory<Logger> { CommonLogger() }

    factory<DispatchersProvider> {
        object : DispatchersProvider {
            override val Main = Dispatchers.Default
            override val Comp = Dispatchers.Default
            override val Io = Dispatchers.IO
        }
    }

} + clientModule

internal inline fun <reified T> KoinComponent.getWithScope() = get<T> { parametersOf(get<CoroutineScope>()) }
