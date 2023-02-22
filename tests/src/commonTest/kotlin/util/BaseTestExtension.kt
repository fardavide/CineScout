package util

import cinescout.database.Database
import cinescout.di.kotlin.CineScoutModule
import io.kotest.core.listeners.BeforeEachListener
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
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
) : BeforeSpecListener, BeforeEachListener, KoinTest {

    override suspend fun beforeSpec(spec: Spec) {
        spec.coroutineTestScope = true
        val appModule = module {
            single<CoroutineScope> { TestScope(UnconfinedTestDispatcher()) }
        }
        startKoin {
            allowOverride(true)
            modules(listOf(appModule, CineScoutModule, extraModule))
        }
        // Initialize the database
        Database.Schema.create(get())
    }

    override suspend fun beforeEach(testCase: TestCase) {
        // Clear the database
        Database.Schema.create(get())
    }
}
