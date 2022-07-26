package cinescout.di.kotlin

import cinescout.utils.kotlin.DispatcherQualifier
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

actual val DispatcherModule = module {

    factory(DispatcherQualifier.Io) { Dispatchers.IO }
}
