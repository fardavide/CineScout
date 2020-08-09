import Test.Actor.EthanSuplee
import Test.Actor.JohnnyDepp
import Test.Actor.PenelopeCruz
import Test.Genre.Crime
import Test.Genre.Drama
import Test.Movie.Blow
import assert4k.*
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test

internal class SuggestEngineTest {

    private val stats = MockStatRepository()
    private val getBestSuggestion = GetBestSuggestion(stats = stats)
    private val rateMovie = RateMovie(stats = stats)

    @Test
    fun `return right suggestion after first movie is rate positively`() = runBlockingTest {
        rateMovie(Blow, Rating.Positive)
        val result = getBestSuggestion(99u)

        assert that result *{
            +actors equals listOf(JohnnyDepp, PenelopeCruz, EthanSuplee)
            +genres equals listOf(Crime, Drama)
            +years equals listOf(FiveYearRange(2005u))
        }
    }

}
