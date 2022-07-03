package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.testdata.MovieTestData.Inception
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class AddMovieToWatchlistTest {

    private val movieRepository: MovieRepository = mockk(relaxUnitFun = true)
    private val addMovieToWatchlist = AddMovieToWatchlist(movieRepository)

    @Test
    fun `does call repository`() = runTest {
        // given
        val movie = Inception

        // when
        addMovieToWatchlist(movie)

        // then
        coVerify { movieRepository.addToWatchlist(movie) }
    }
}
