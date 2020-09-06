package client.android

import client.clientModule
import co.touchlab.kermit.LogcatLogger
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.Koin
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import util.DispatchersProvider

val androidClientModule = module {

    factory<Logger> { LogcatLogger() }

    factory<DispatchersProvider> {
        object : DispatchersProvider {
            override val Comp = Dispatchers.Default
            override val Io = Dispatchers.IO
            override val Main: CoroutineDispatcher = Dispatchers.Main
        }
    }

} + clientModule

/**
 * Call [Koin.get] passing a [CoroutineScope] as parameter
 * Short version of `` get { parametersOf(coroutineScope) } ``
 */
internal inline fun <reified T> Koin.getWithScope(scope: CoroutineScope) =
    get<T> { parametersOf(scope) }

internal typealias Get<T> = (CoroutineScope) -> T
