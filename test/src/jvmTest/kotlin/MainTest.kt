import Test.Movie.AmericanGangster
import Test.Movie.Inception
import Test.Movie.TheBookOfEli
import kotlinx.coroutines.runBlocking
import movies.remote.tmdb.tmdbRemoteMoviesModule
import org.koin.core.context.startKoin
import kotlin.test.Test

class MainTest {

    private val koin = startKoin {
        modules(domainModule + domainMockStatModule + tmdbRemoteMoviesModule)
    }.koin

    @Test
    fun `run main`() = runBlocking {
        val rateMovie = koin.get<RateMovie>()
        val getSuggestedMovies = koin.get<GetSuggestedMovies>()

        rateMovie(AmericanGangster, Rating.Positive)
        rateMovie(Inception, Rating.Positive)
        rateMovie(TheBookOfEli, Rating.Positive)

        val result = getSuggestedMovies(dataLimit = 3u)
            .withIndex()
            .joinToString(
                prefix = "\n\n",
                postfix = "\n\n",
                separator = "\n\n"
            ) { (i, movie) ->
                """
                Result $i: ${movie.name.s}
                Cast: ${movie.actors.take(3).joinToString { it.name.s }}
                Genres: ${movie.genres.joinToString { it.name.s }}
                """.trimIndent()
            }
        println(result)
    }
}
