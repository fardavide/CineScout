package cinescout.di

import arrow.core.right
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.database.Database
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import cinescout.movies.domain.usecase.GetMovie
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Ignore
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.DurationUnit.SECONDS
import kotlin.time.toDuration

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
    @Ignore
    fun `get movie`() = runTest {
        // given
        val movie = MovieTestData.TheWolfOfWallStreet
        val getMovie: GetMovie = get()

        // when
        val result = getMovie(TmdbMovieIdTestData.TheWolfOfWallStreet).first()

        // then
        assertEquals(movie.right(), result)
    }

    @Test
    @Ignore
    fun `link to Tmdb`() = runBlocking {
        // given
        val linkToTmdb: LinkToTmdb = get()

        // when
        linkToTmdb().collectLatest { either ->

            // then
            either.fold(
                ifLeft = { println(it) },
                ifRight = { state ->
                    when (state) {
                        is LinkToTmdb.State.UserShouldAuthorizeToken -> {
                            println(state.authorizationUrl)
                            delay(10.toDuration(SECONDS))
                            state.authorizationResultChannel.send(LinkToTmdb.TokenAuthorized.right())
                        }
                        LinkToTmdb.State.Success -> println("SUCCESS!")
                    }
                }
            )
        }
    }
}
