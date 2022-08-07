package cinescout.movies.domain.usecase

import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.testdata.MovieTestData.Inception
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class AddMovieToWatchlistTest {

    private val movieRepository: MovieRepository = mockk {
        coEvery { addToWatchlist(any()) } returns Unit.right()
    }
    private val addMovieToWatchlist = AddMovieToWatchlist(movieRepository)

    @Test
    fun `does call repository`() = runTest {
        // given
        val movieId = Inception.tmdbId

        // when
        addMovieToWatchlist(movieId)

        // then
        coVerify { movieRepository.addToWatchlist(movieId) }
    }
}
