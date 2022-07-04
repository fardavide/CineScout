package cinescout.di

import arrow.core.right
import cinescout.database.Database
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

// TODO: Move to proper module
class AppTest : AutoCloseKoinTest() {

    private val koinInstance = startKoin {
        modules(CineScoutModule)
    }.koin

    override fun getKoin() = koinInstance

    @BeforeTest
    fun setup() {
        Database.Schema.create(get())
    }

    @Test
//    @Ignore
    fun `get movie`() = runTest {
        // given
        val movie = MovieTestData.TheWolfOfWallStreet
        val repository: MovieRepository = get()

        // when
        val result = repository.getMovie(TmdbMovieIdTestData.TheWolfOfWallStreet).first()

        // then
        assertEquals(movie.right(), result)
    }
}
