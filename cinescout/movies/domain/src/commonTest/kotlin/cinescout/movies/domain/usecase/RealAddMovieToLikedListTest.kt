package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.sample.MovieSample
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class RealAddMovieToLikedListTest {

    private val movieRepository: MovieRepository = mockk(relaxUnitFun = true)
    private val addMovieToLikedList = RealAddMovieToLikedList(movieRepository)

    @Test
    fun `does call repository`() = runTest {
        // given
        val movieId = MovieSample.Inception.tmdbId

        // when
        addMovieToLikedList(movieId)

        // then
        coVerify { movieRepository.addToLiked(movieId) }
    }
}
