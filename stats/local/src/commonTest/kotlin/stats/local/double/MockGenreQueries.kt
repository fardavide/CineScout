package stats.local.double

import database.movies.GenreQueries
import entities.IntId
import entities.TmdbId
import entities.model.Name
import io.mockk.every
import io.mockk.mockk

fun mockGenreQueries(genres: MutableList<Pair<TmdbId, Name>>): GenreQueries = mockk {

    every { insert(TmdbId(any()), Name(any())) } answers {
        val idArg = TmdbId(firstArg())
        val nameArg = Name(secondArg())
        val index = genres.indexOf { (tmdbId, name) -> tmdbId == idArg && name == nameArg }
        genres.insert(index, idArg to nameArg)
    }

    every { selectIdByTmdbId(TmdbId(any())) } answers {
        val tmdbIdArg = TmdbId(firstArg())
        mockk {
            every { executeAsOne() } answers {
                IntId(genres.indexOf { it.first == tmdbIdArg }!!)
            }
        }
    }
}
