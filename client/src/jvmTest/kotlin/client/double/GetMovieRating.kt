package client.double

import domain.stats.GetMovieRating
import entities.model.UserRating
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

fun StubGetMovieRating(result: UserRating) = mockk<GetMovieRating> {
    every { this@mockk.invoke(any()) } returns flowOf(result)
}
