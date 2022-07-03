package cinescout.di

import org.koin.test.KoinTest
import org.koin.test.check.checkKoinModules
import kotlin.test.Test

class CineScoutModuleTest : KoinTest {

    @Test
    fun verify() {
        checkKoinModules(listOf(CineScoutModule))
    }
}
