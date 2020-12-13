package client.double

import domain.FindMovie
import entities.TmdbId
import entities.movies.Movie
import entities.right
import io.mockk.coEvery
import io.mockk.mockk

fun StubFindMovie(result: Movie) = mockk<FindMovie> {
    coEvery { this@mockk.invoke(TmdbId(any())) } returns result.right()
}
