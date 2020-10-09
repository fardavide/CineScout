package client.double

import domain.stats.IsMovieInWatchlist
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

fun StubIsMovieInWatchlist(result: Boolean) = mockk<IsMovieInWatchlist> {
    every { this@mockk.invoke(any()) } returns flowOf(result)
}
