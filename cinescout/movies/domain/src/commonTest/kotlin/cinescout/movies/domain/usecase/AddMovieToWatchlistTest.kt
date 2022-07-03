package cinescout.movies.domain.usecase

import cinescout.movies.domain.testdata.MovieTestData.Inception
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class AddMovieToWatchlistTest {

    private val addMovieToWatchlist = AddMovieToWatchlist()

    @Test
    fun `does nothing`() = runTest {
        addMovieToWatchlist(Inception)
    }
}
