package stats.local.double

import database.movies.Movie_genreQueries
import entities.IntId
import io.mockk.every
import io.mockk.mockk

fun mockMovieGenreQueries(movieGenres: MutableList<Pair<IntId, IntId>>): Movie_genreQueries = mockk {

    every { insert(IntId(any()), IntId(any())) } answers {
        val movieIdArg = IntId(firstArg())
        val genreIdArg = IntId(secondArg())
        val index =
            movieGenres.indexOf { (movieId, genreId) -> movieId == movieIdArg && genreId == genreIdArg }
        movieGenres.insert(index, movieIdArg to genreIdArg)
    }
}
