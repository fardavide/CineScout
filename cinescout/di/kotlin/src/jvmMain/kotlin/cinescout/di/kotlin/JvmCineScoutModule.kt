package cinescout.di.kotlin

import cinescout.utils.kotlin.DispatcherQualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import org.koin.dsl.module

@OptIn(DelicateCoroutinesApi::class)
actual val DispatcherModule = module {

    factory(DispatcherQualifier.Io) { Dispatchers.IO }
    single<CoroutineDispatcher>(DispatcherQualifier.DatabaseWrite) { newSingleThreadContext("Database write") }
}
