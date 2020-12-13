import assert4k.*
import co.touchlab.kermit.CommonLogger
import domain.DiscoverMovies
import domain.MockMovieRepository
import domain.MockStatRepository
import domain.Test.Actor.DenzelWashington
import domain.Test.Actor.EthanSuplee
import domain.Test.Actor.JohnnyDepp
import domain.Test.Actor.LeonardoDiCaprio
import domain.Test.Actor.PenelopeCruz
import domain.Test.Actor.SamuelLJackson
import domain.Test.Genre.Action
import domain.Test.Genre.Crime
import domain.Test.Genre.Drama
import domain.Test.Genre.Thriller
import domain.Test.Movie.Blow
import domain.Test.Movie.DejaVu
import domain.Test.Movie.DjangoUnchained
import domain.Test.Movie.Inception
import domain.Test.Movie.JohnWick
import domain.Test.Movie.PulpFiction
import domain.Test.Movie.SinCity
import domain.Test.Movie.TheBookOfEli
import domain.Test.Movie.TheGreatDebaters
import domain.Test.Movie.TheHatefulEight
import domain.stats.GenerateDiscoverParams
import domain.stats.GenerateMoviesSuggestions
import domain.stats.GetSuggestionData
import domain.stats.RateMovie
import entities.model.FiveYearRange
import entities.model.UserRating
import entities.model.UserRating.Positive
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.*

internal class SuggestEngineTest {

    private val stats = MockStatRepository()
    private val rateMovie = RateMovie(stats = stats)

    private val getSuggestionData = GetSuggestionData(stats = stats)

    // region rateMovie - getSuggestionData
    @Test
    fun `return right suggestion after first movie is rate positively`() = runBlockingTest {
        rateMovie(Blow, Positive)
        val result = getSuggestionData(99).rightOrThrow()

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
        ).forEach { rateMovie(it, Positive) }
        val result = getSuggestionData(3).rightOrThrow()

        assert that result * {
            +actors `equals no order` setOf(DenzelWashington, LeonardoDiCaprio, SamuelLJackson)
            +genres `equals no order` setOf(Action, Thriller, Crime)
            +years `contains all` setOf(FiveYearRange(2010u), FiveYearRange(2015u))
        }
    }

    @Test
    fun `rating positively more times the same movies doesn't increases stats`() = runBlockingTest {
        rateMovie(DejaVu, Positive)
        rateMovie(TheBookOfEli, Positive)
        rateMovie(TheGreatDebaters, Positive)

        val result1 = getSuggestionData(1).rightOrThrow()
        assert that result1.actors.first() equals DenzelWashington

        repeat(5) {
            rateMovie(Inception, Positive)
        }

        val result2 = getSuggestionData(1).rightOrThrow()
        assert that result2.actors.first() equals DenzelWashington
    }

    @Test
    fun `rating negatively a movies previously rated positively decreases stats`() = runBlockingTest {
        rateMovie(TheBookOfEli, Positive)
        rateMovie(TheGreatDebaters, Positive)
        rateMovie(Inception, Positive)

        // DenzelWashington 2
        // LeonardoDiCaprio 1
        val result1 = getSuggestionData(1).rightOrThrow()
        assert that result1.actors.first() equals DenzelWashington

        rateMovie(TheBookOfEli, UserRating.Negative)
        rateMovie(TheGreatDebaters, UserRating.Negative)
        rateMovie(DjangoUnchained, Positive)

        val result2 = getSuggestionData(1).rightOrThrow()
        assert that result2.actors.first() equals LeonardoDiCaprio
    }

    @Test
    fun `rating positively a movies negatively rated positively increases stats`() = runBlockingTest {
        rateMovie(TheBookOfEli, UserRating.Negative)
        rateMovie(DjangoUnchained, Positive)
        rateMovie(Inception, Positive)

        // DenzelWashington -1
        // LeonardoDiCaprio 2
        val result1 = getSuggestionData(1).rightOrThrow()
        assert that result1.actors.first() equals LeonardoDiCaprio

        rateMovie(TheBookOfEli, Positive)
        rateMovie(DejaVu, Positive)
        rateMovie(TheGreatDebaters, Positive)

        // DenzelWashington 3
        // LeonardoDiCaprio 2
        val result2 = getSuggestionData(1).rightOrThrow()
        assert that result2.actors.first() equals DenzelWashington
    }
    // endregion

    private val generateMoviesSuggestions = GenerateMoviesSuggestions(
        discover = DiscoverMovies(movies = MockMovieRepository()),
        generateDiscoverParams = GenerateDiscoverParams(randomize = false),
        getSuggestionsData = getSuggestionData,
        stats = stats,
        logger = CommonLogger()
    )


    // region getSuggestedMovies
    @Test
    fun `does not return already rated movies`() = runBlockingTest {
        rateMovie(Inception, Positive)
        assert that generateMoviesSuggestions().rightOrThrow() `is` empty
    }


    @Test
    fun `return closest movie as the rated ones`() = runBlockingTest {
        rateMovie(DejaVu, Positive)
        rateMovie(TheGreatDebaters, Positive)
        rateMovie(JohnWick, Positive)
        assert that generateMoviesSuggestions().rightOrThrow().first() equals TheBookOfEli
    }
    // endregion
}
