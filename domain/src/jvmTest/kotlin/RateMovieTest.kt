import assert4k.*
import domain.MockStatRepository
import domain.Test.Movie.Blow
import domain.stats.RateMovie
import entities.model.UserRating
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.*

internal class RateMovieTest {

    private val stats = MockStatRepository()
    private val rateMovie = RateMovie(stats = stats)

    @Test
    fun `movie is saved when rated`() = runBlockingTest {
        rateMovie(Blow, UserRating.Positive)
        assert that stats.ratedMovies() contains (Blow to UserRating.Positive)
    }

    @Test
    fun `movie is update when rated`() = runBlockingTest {
        rateMovie(Blow, UserRating.Negative)
        rateMovie(Blow, UserRating.Positive)

        assert that stats.ratedMovies() * {
            it contains(Blow to UserRating.Positive)
            +size() equals 1
        }
    }
}
