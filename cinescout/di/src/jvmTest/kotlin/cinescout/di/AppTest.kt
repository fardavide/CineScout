package cinescout.di

import arrow.core.right
import cinescout.movies.data.remote.TmdbRemoteMovieDataSource
import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.Test
import kotlin.test.assertEquals

// TODO: Move to proper module
class AppTest : KoinTest {

    override fun getKoin() = startKoin {
        modules(CineScoutModule)
    }.koin

    @Test
//    @Ignore
    fun `get movie from Tmdb`() = runTest {
        // given
        val movie = TmdbMovieTestData.TheWolfOfWallStreet
        val repository: TmdbRemoteMovieDataSource = get()

        // when
        val result = repository.getMovie(movie.id)

        // then
        assertEquals(movie.right(), result)
    }
}
