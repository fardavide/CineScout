package stats

import Actor
import FiveYearRange
import Genre
import Rating
import movies.Movie

interface StatRepository {

    suspend fun topActors(limit: UInt): Collection<Actor>
    suspend fun topGenres(limit: UInt): Collection<Genre>
    suspend fun topYears(limit: UInt): Collection<FiveYearRange>
    suspend fun ratedMovies(): Collection<Pair<Movie, Rating>>
    suspend fun rate(movie: Movie, rating: Rating)
}

val Collection<Pair<Movie, Rating>>.movies get() =
    map { it.first }
