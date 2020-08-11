package movies

import FiveYearRange
import Name

interface MovieRepository {

    suspend fun searchMovie(
        actors: Collection<Name> = emptySet(),
        genres: Collection<Name> = emptySet(),
        years: FiveYearRange? = null
    ): Collection<Movie>
}
