package cinescout.di

import cinescout.di.kotlin.CineScoutModule
import org.koin.core.error.InstanceCreationException
import org.koin.test.KoinTest
import org.koin.test.check.checkKoinModules
import kotlin.test.Test

class CineScoutModuleTest : KoinTest {

    @Test
    fun `verify common modules`() {
        try {
            checkKoinModules(listOf(CineScoutModule))
        } catch (e: InstanceCreationException) {
            throw e.getRootCause()
        }
    }

    private fun Throwable.getRootCause(): Throwable {
        var rootCause: Throwable? = this
        while (rootCause?.cause != null) {
            rootCause = rootCause.cause
        }
        return checkNotNull(rootCause)
    }
}
