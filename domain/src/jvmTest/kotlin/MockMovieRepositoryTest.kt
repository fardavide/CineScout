import Test.Actor.DenzelWashington
import Test.Actor.JohnTravolta
import Test.Actor.LeonardoDiCaprio
import Test.Genre.Action
import Test.Movie.Blow
import Test.Movie.DejaVu
import Test.Movie.DjangoUnchained
import Test.Movie.Inception
import Test.Movie.PulpFiction
import Test.Movie.SinCity
import Test.Movie.TheBookOfEli
import Test.Movie.TheGreatDebaters
import Test.Movie.Willard
import assert4k.*
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test

internal class MockMovieRepositoryTest {

    private val movies = MockMovieRepository()

    @Test
    fun `returns right movies by single actor`() = runBlockingTest {
        val result = movies.searchMovie(
            actors = setOf(DenzelWashington)
        )

        assert that result * {
            +size() equals 3
            it `equals no order` setOf(
                DejaVu,
                TheBookOfEli,
                TheGreatDebaters,
            )
        }
    }

    @Test
    fun `returns right movies by single actor and single genre`() = runBlockingTest {
        val result = movies.searchMovie(
            actors = setOf(DenzelWashington),
            genres = setOf(Action)
        )

        assert that result * {
            +size() equals 2
            it `equals no order` setOf(
                DejaVu,
                TheBookOfEli,
            )
        }
    }

    @Test
    fun `returns right movies by multi actor`() = runBlockingTest {
        val result = movies.searchMovie(
            actors = setOf(DenzelWashington, JohnTravolta)
        )

        assert that result * {
            +size() equals 4
            it `equals no order` setOf(
                DejaVu,
                TheBookOfEli,
                TheGreatDebaters,
                PulpFiction
            )
        }
    }

    @Test
    fun `returns right movies by multi actor and multi genres`() = runBlockingTest {
        val result = movies.searchMovie(
            actors = setOf(DenzelWashington, LeonardoDiCaprio),
            genres = setOf(Action)
        )

        assert that result * {
            +size() equals 3
            it `equals no order` setOf(
                DejaVu,
                Inception,
                TheBookOfEli,
            )
        }
    }

    @Test
    fun `returns right movies by year`() = runBlockingTest {
        val result = movies.searchMovie(
            years = FiveYearRange(2005u)
        )

        assert that result * {
            +size() equals 3
            it `equals no order` setOf(
                Blow,
                SinCity,
                Willard,
            )
        }
    }

    @Test
    fun `returns right movies by multi actor and year`() = runBlockingTest {
        val result = movies.searchMovie(
            actors = setOf(DenzelWashington, LeonardoDiCaprio),
            years = FiveYearRange(2015u)
        )

        assert that result * {
            +size() equals 3
            it `equals no order` setOf(
                DjangoUnchained,
                Inception,
                TheBookOfEli,
            )
        }
    }

    @Test
    fun `returns right movies by multi actor, genre and year`() = runBlockingTest {
        val result = movies.searchMovie(
            actors = setOf(DenzelWashington, LeonardoDiCaprio),
            genres = setOf(Action),
            years = FiveYearRange(2015u)
        )

        assert that result * {
            +size() equals 2
            it `equals no order` setOf(
                Inception,
                TheBookOfEli,
            )
        }
    }
}
