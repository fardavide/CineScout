package cinescout.di

import cinescout.movies.data.remote.TmdbRemoteMovieDataSource
import cinescout.movies.data.remote.model.TmdbMovieId
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.error.InstanceCreationException
import org.koin.test.KoinTest
import org.koin.test.check.checkKoinModules
import kotlin.test.Ignore
import kotlin.test.Test

class CineScoutModuleTest : KoinTest {

    @Test
    fun verify() {
        @Suppress("SwallowedException")
        try {
            checkKoinModules(listOf(CineScoutModule))
        } catch (e: InstanceCreationException) {
            throw e.getRootCause()
        }
    }

    @Test
    @Ignore
    fun run() = runTest {
        val koin = startKoin {
            modules(CineScoutModule)
        }.koin

        val repository: TmdbRemoteMovieDataSource = koin.get()
        println(repository.getMovie(TmdbMovieId(106_646)))
    }

    private fun Throwable.getRootCause(): Throwable {
        var rootCause: Throwable? = this
        while (rootCause?.cause != null) {
            rootCause = rootCause.cause
        }
        return checkNotNull(rootCause)
    }
}
