package util

import cinescout.database.Database
import cinescout.di.kotlin.CineScoutModule
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.spec.Spec
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get

class BaseTestExtension(
    private val extraModule: Module = Module()
) : BeforeSpecListener, KoinTest {

    override suspend fun beforeSpec(spec: Spec) {
        val appModule = module {
            single<CoroutineScope> { TestScope(UnconfinedTestDispatcher()) }
        }
        startKoin {
            allowOverride(true)
            modules(listOf(appModule, CineScoutModule, extraModule))
        }
        Database.Schema.create(get())
    }
}
