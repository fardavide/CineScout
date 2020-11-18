package stats.local.double

import database.movies.ActorQueries
import entities.IntId
import entities.TmdbId
import entities.model.Name
import io.mockk.every
import io.mockk.mockk

fun mockActorQueries(actors: MutableList<Pair<TmdbId, Name>>): ActorQueries = mockk {

    every { insert(TmdbId(any()), Name(any())) } answers {
        val idArg = TmdbId(firstArg())
        val nameArg = Name(secondArg())
        val index = actors.indexOf { (tmdbId, name) -> tmdbId == idArg && name == nameArg }
        actors.insert(index, idArg to nameArg)
    }

    every { selectIdByTmdbId(TmdbId(any())) } answers {
        val tmdbIdArg = TmdbId(firstArg())
        mockk {
            every { executeAsOne() } answers {
                IntId(actors.indexOf { it.first == tmdbIdArg }!!)
            }
        }
    }

}
