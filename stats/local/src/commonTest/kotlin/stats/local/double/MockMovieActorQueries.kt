package stats.local.double

import database.movies.Movie_actorQueries
import entities.IntId
import io.mockk.every
import io.mockk.mockk

fun mockMovieActorQueries(movieActors: MutableList<Pair<IntId, IntId>>): Movie_actorQueries = mockk {

    every { insert(IntId(any()), IntId(any())) } answers {
        val movieIdArg = IntId(firstArg())
        val actorIdArg = IntId(secondArg())
        val index =
            movieActors.indexOf { (movieId, actorId) -> movieId == movieIdArg && actorId == actorIdArg }
        movieActors.insert(index, movieIdArg to actorIdArg)
    }
}
