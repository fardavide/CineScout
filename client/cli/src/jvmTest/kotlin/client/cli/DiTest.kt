package client.cli

import org.junit.experimental.categories.Category
import org.koin.test.AutoCloseKoinTest
import org.koin.*
import kotlin.test.*
import assert4k.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.*
import org.koin.test.category.*
import org.koin.*
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.parameter.parametersOf
import org.koin.test.check.checkModules
import sun.util.logging.PlatformLogger

@Category(CheckModuleTest::class)
class DiTest : AutoCloseKoinTest() {

    @Test
    fun checkModules() = checkModules(level = Level.DEBUG, parameters = {
        parametersOf(CoroutineScope(Job()))
    }) {
        modules(cliClientModule)
    }
}
