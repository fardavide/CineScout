package cinescout.di

import cinescout.home.presentation.HomePresentationModule
import cinescout.utils.kotlin.DispatcherQualifier
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

actual val AndroidModule = module {
    includes(HomePresentationModule)
}

actual val DispatcherModule = module {

    factory(DispatcherQualifier.Io) { Dispatchers.IO }
}
