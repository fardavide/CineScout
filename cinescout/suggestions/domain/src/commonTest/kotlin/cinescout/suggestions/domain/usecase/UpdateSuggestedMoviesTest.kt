package cinescout.suggestions.domain.usecase

import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.testdata.MovieTestData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class UpdateSuggestedMoviesTest {

    private val generateSuggestedMovies: GenerateSuggestedMovies = mockk {
        val movies = nonEmptyListOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet,
            MovieTestData.War
        )
        every { invoke(suggestionsMode = any()) } returns flowOf(movies.right())
    }
    private val movieRepository: MovieRepository = mockk(relaxUnitFun = true)
    private val updateSuggestedMovies = UpdateSuggestedMovies(
        generateSuggestedMovies = generateSuggestedMovies,
        movieRepository = movieRepository
    )


}
