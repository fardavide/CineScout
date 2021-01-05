package domain.stats

import assert4k.*
import domain.Test.Movie.DejaVu
import domain.Test.Movie.SinCity
import domain.Test.Movie.TheBookOfEli
import domain.Test.Movie.TheHatefulEight
import domain.stats.MoviesSorting.Alphabetical
import domain.stats.MoviesSorting.Direction.Ascendant
import domain.stats.MoviesSorting.Direction.Descendant
import domain.stats.MoviesSorting.Popularity
import domain.stats.MoviesSorting.Rating
import domain.stats.MoviesSorting.ReleaseDate
import entities.Either
import entities.movies.Movie
import entities.right
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import util.test.CoroutinesTest

internal class GetMoviesInWatchlistTest : CoroutinesTest {

    private val getMoviesInWatchlist = GetMoviesInWatchlist(
        mockk {
            every { watchlist() } returns flowOf(listOf(
                DejaVu,
                SinCity,
                TheBookOfEli,
                TheHatefulEight
            ).right())
        }
    )

    @Test
    fun `movies are sorted alphabetically ascending`() = coroutinesTest {
        val result = getMoviesInWatchlist(Alphabetical(Ascendant)).get()
        assert that result equals listOf(
            DejaVu,
            SinCity,
            TheBookOfEli,
            TheHatefulEight,
        )
    }

    @Test
    fun `movies are sorted alphabetically descending`() = coroutinesTest {
        val result = getMoviesInWatchlist(Alphabetical(Descendant)).get()
        assert that result equals listOf(
            TheHatefulEight,
            TheBookOfEli,
            SinCity,
            DejaVu
        )
    }

    @Test
    fun `movies are sorted by popularity ascending`() = coroutinesTest {
        val result = getMoviesInWatchlist(Popularity(Ascendant)).get()
        assert that result equals listOf(
            DejaVu,
            TheBookOfEli,
            TheHatefulEight,
            SinCity
        )
    }

    @Test
    fun `movies are sorted by popularity descending`() = coroutinesTest {
        val result = getMoviesInWatchlist(Popularity(Descendant)).get()
        assert that result equals listOf(
            DejaVu,
            TheBookOfEli,
            TheHatefulEight,
            SinCity
        )
    }

    @Test
    fun `movies are sorted by rating ascending`() = coroutinesTest {
        val result = getMoviesInWatchlist(Rating(Ascendant)).get()
        assert that result equals listOf(
            DejaVu,
            TheHatefulEight,
            TheBookOfEli,
            SinCity
        )
    }

    @Test
    fun `movies are sorted by rating descending`() = coroutinesTest {
        val result = getMoviesInWatchlist(Rating(Descendant)).get()
        assert that result equals listOf(
            SinCity,
            TheBookOfEli,
            TheHatefulEight,
            DejaVu
        )
    }

    @Test
    fun `movies are sorted by release date ascending`() = coroutinesTest {
        val result = getMoviesInWatchlist(ReleaseDate(Ascendant)).get()
        assert that result equals listOf(
            SinCity,
            DejaVu,
            TheBookOfEli,
            TheHatefulEight
        )
    }

    @Test
    fun `movies are sorted by release date descending`() = coroutinesTest {
        val result = getMoviesInWatchlist(ReleaseDate(Descendant)).get()
        assert that result equals listOf(
            TheHatefulEight,
            TheBookOfEli,
            DejaVu,
            SinCity,
        )
    }


    private suspend fun Flow<Either<GetMoviesInWatchlist.Error, GetMoviesInWatchlist.State>>.get(): Collection<Movie> =
        (first().rightOrThrow() as GetMoviesInWatchlist.State.Success).movies
}
