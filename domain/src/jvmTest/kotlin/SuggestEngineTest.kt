import Test.Actor.DenzelWashington
import Test.Actor.EthanSuplee
import Test.Actor.JohnnyDepp
import Test.Actor.LeonardoDiCaprio
import Test.Actor.PenelopeCruz
import Test.Actor.SamuelLJackson
import Test.Genre.Action
import Test.Genre.Crime
import Test.Genre.Drama
import Test.Genre.Thriller
import Test.Movie.Blow
import Test.Movie.DejaVu
import Test.Movie.DjangoUnchained
import Test.Movie.Inception
import Test.Movie.JohnWick
import Test.Movie.PulpFiction
import Test.Movie.SinCity
import Test.Movie.TheBookOfEli
import Test.Movie.TheGreatDebaters
import Test.Movie.TheHatefulEight
import assert4k.*
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test

internal class SuggestEngineTest {

    private val stats = MockStatRepository()
    private val getSuggestionData = GetSuggestionData(stats = stats)
    private val rateMovie = RateMovie(stats = stats)

    // region rateMovie - getSuggestionData
    @Test
    fun `return right suggestion after first movie is rate positively`() = runBlockingTest {
        rateMovie(Blow, Rating.Positive)
        val result = getSuggestionData(99u)

        assert that result * {
            +actors equals listOf(JohnnyDepp, PenelopeCruz, EthanSuplee)
            +genres equals listOf(Crime, Drama)
            +years equals listOf(FiveYearRange(2005u))
        }
    }

    @Test
    fun `return right 3 suggestion after 10 movies rated positively`() = runBlockingTest {
        setOf(
            Blow,
            DejaVu,
            DjangoUnchained,
            Inception,
            JohnWick,
            PulpFiction,
            SinCity,
            TheBookOfEli,
            TheGreatDebaters,
            TheHatefulEight,
        ).forEach { rateMovie(it, Rating.Positive) }
        val result = getSuggestionData(3u)

        assert that result * {
            +actors `equals no order` setOf(DenzelWashington, LeonardoDiCaprio, SamuelLJackson)
            +genres `equals no order` setOf(Action, Thriller, Drama)
            +years `contains all` setOf(FiveYearRange(2010u), FiveYearRange(2015u))
        }
    }

    @Test
    fun `rating positively more times the same movies doesn't increases stats`() = runBlockingTest {
        rateMovie(DejaVu, Rating.Positive)
        rateMovie(TheBookOfEli, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)

        val result1 = getSuggestionData(1u)
        assert that result1.actors.first() equals DenzelWashington

        repeat(5) {
            rateMovie(Inception, Rating.Positive)
        }

        val result2 = getSuggestionData(1u)
        assert that result2.actors.first() equals DenzelWashington
    }

    @Test
    fun `rating negatively a movies previously rated positively decreases stats`() = runBlockingTest {
        rateMovie(TheBookOfEli, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)
        rateMovie(Inception, Rating.Positive)

        // DenzelWashington 2
        // LeonardoDiCaprio 1
        val result1 = getSuggestionData(1u)
        assert that result1.actors.first() equals DenzelWashington

        rateMovie(TheBookOfEli, Rating.Negative)
        rateMovie(TheGreatDebaters, Rating.Negative)
        rateMovie(DjangoUnchained, Rating.Positive)

        val result2 = getSuggestionData(1u)
        assert that result2.actors.first() equals LeonardoDiCaprio
    }

    @Test
    fun `rating positively a movies negatively rated positively increases stats`() = runBlockingTest {
        rateMovie(TheBookOfEli, Rating.Negative)
        rateMovie(DjangoUnchained, Rating.Positive)
        rateMovie(Inception, Rating.Positive)

        // DenzelWashington -1
        // LeonardoDiCaprio 2
        val result1 = getSuggestionData(1u)
        assert that result1.actors.first() equals LeonardoDiCaprio

        rateMovie(TheBookOfEli, Rating.Positive)
        rateMovie(DejaVu, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)

        // DenzelWashington 3
        // LeonardoDiCaprio 2
        val result2 = getSuggestionData(1u)
        assert that result2.actors.first() equals DenzelWashington
    }
    // endregion
}
