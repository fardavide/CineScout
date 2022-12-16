package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.sample.MovieSample
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class AddMovieToDislikedListTest {

    private val movieRepository: MovieRepository = mockk(relaxUnitFun = true)
    private val addMovieToDislikedList = AddMovieToDislikedList(movieRepository)

    @Test
    fun `does call repository`() = runTest {
        // given
        val movieId = MovieSample.Inception.tmdbId

        // when
        addMovieToDislikedList(movieId)

        // then
        coVerify { movieRepository.addToDisliked(movieId) }
    }
}
