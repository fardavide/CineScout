package cinescout.movies.domain.usecase

import cinescout.movies.domain.model.Movie
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class AddMovieToWatchlistTest {

    private val addMovieToWatchlist = AddMovieToWatchlist()

    @Test
    fun `does nothing`() = runTest {
        addMovieToWatchlist(Movie("Inception"))
    }
}
