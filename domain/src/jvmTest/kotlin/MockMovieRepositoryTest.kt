import assert4k.*
import domain.MockMovieRepository
import domain.Test.Actor.DenzelWashington
import domain.Test.Genre.Action
import domain.Test.Movie.AmericanGangster
import domain.Test.Movie.Blow
import domain.Test.Movie.DejaVu
import domain.Test.Movie.Fury
import domain.Test.Movie.Inception
import domain.Test.Movie.PulpFiction
import domain.Test.Movie.SinCity
import domain.Test.Movie.TheBookOfEli
import domain.Test.Movie.TheEqualizer
import domain.Test.Movie.TheGreatDebaters
import domain.Test.Movie.TheHatefulEight
import domain.Test.Movie.War
import entities.left
import entities.movies.DiscoverParams
import entities.movies.SearchError
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.*

internal class MockMovieRepositoryTest {

    private val movies = MockMovieRepository()

    // region discover
    @Test
    fun `returns right movies by single actor and single genre`() = runBlockingTest {
        val result = movies.discover(DiscoverParams(DenzelWashington.id, Action.id)).rightOrThrow()

        assert that result * {
            +size() equals 3
            it `equals no order` setOf(
                DejaVu,
                TheBookOfEli,
                TheEqualizer
            )
        }
    }
    // endregion

    // region search
    @Test
    fun `search by empty query give no results`() = runBlockingTest {
        val result = movies.search("")
        assert that result equals SearchError.EmptyQuery.left()
    }

    @Test
    fun `search by title`() = runBlockingTest {
        val result = movies.search("Inception").rightOrThrow()
        assert that result *{
            +size() equals 1
            +first() equals Inception
        }
    }

    @Test
    fun `search by actor name`() = runBlockingTest {
        val result = movies.search("Denzel").rightOrThrow()
        assert that result* {
            +size() equals 5
            it `equals no order` setOf(AmericanGangster, DejaVu, TheBookOfEli, TheEqualizer, TheGreatDebaters)
        }
    }

    @Test
    fun `search by genre name`() = runBlockingTest {
        val result = movies.search("Crime").rightOrThrow()
        assert that result* {
            +size() equals 6
            it `equals no order` setOf(
                AmericanGangster,
                Blow,
                PulpFiction,
                SinCity,
                TheEqualizer,
                TheHatefulEight
            )
        }
    }

    @Test
    fun `search by title or genre`() = runBlockingTest {
        val result = movies.search("War").rightOrThrow()
        assert that result*{
            +size() equals 2
            it `equals no order` setOf(Fury, War)
        }
    }

    @Test
    fun `search works with different case`() = runBlockingTest {
        val result = movies.search("iNcePtIoN").rightOrThrow()
        assert that result *{
            +size() equals 1
            +first() equals Inception
        }
    }

    @Test
    fun `search works with empty spaces`() = runBlockingTest {
        val result1 = movies.search("   Denzel").rightOrThrow()
        assert that result1.size equals 5

        val result2 = movies.search("   Denzel   ").rightOrThrow()
        assert that result2.size equals 5

        val result3 = movies.search("   Denzel    Washington   ").rightOrThrow()
        assert that result3.size equals 5
    }
    // endregion
}
