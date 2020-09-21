package client.double

import domain.GetMovieRating
import domain.IsMovieInWatchlist
import entities.Rating
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

fun StubGetMovieRating(result: Rating) = mockk<GetMovieRating> {
    every { this@mockk.invoke(any()) } returns flowOf(result)
}
