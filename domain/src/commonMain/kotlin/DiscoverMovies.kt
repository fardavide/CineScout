import movies.MovieRepository

class DiscoverMovies(
    private val movies: MovieRepository
) {

    suspend operator fun invoke(
        actors: Collection<Name> = emptySet(),
        genres: Collection<Name> = emptySet(),
        years: FiveYearRange? = null
    ) = movies.discover(actors, genres, years)
}
