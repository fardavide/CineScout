package movies

import FiveYearRange
import Name

interface MovieRepository {

    suspend fun discover(
        actors: Collection<Name> = emptySet(),
        genres: Collection<Name> = emptySet(),
        years: FiveYearRange? = null
    ): Collection<Movie>

    suspend fun search(query: String): Collection<Movie>
}
