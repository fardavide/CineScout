package cinescout.di

import cinescout.di.kotlin.CineScoutModule
import cinescout.lists.domain.ListSorting
import cinescout.perfomance.FakePerformance
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.suggestions.domain.usecase.ScheduleUpdateSuggestions
import kotlinx.coroutines.test.TestScope
import org.koin.core.error.InstanceCreationException
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.check.checkKoinModules
import kotlin.test.Test

class CineScoutModuleTest : KoinTest {

    private val extraModule = module {
        factory { ScheduleUpdateSuggestions {} }
    }

    @Test
    fun `verify common modules`() {
        try {
            checkKoinModules(listOf(CineScoutModule().module, extraModule)) {
                withInstance(123) // app version
                withInstance(ListSorting.Rating.Descending)
                withInstance(FakePerformance())
                withInstance(ScreenplayTypeFilter.All)
                withInstance(TestScope())
            }
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
