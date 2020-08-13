import movies.Movie
import movies.MovieRepository
import suggestions.SuggestionData

class DiscoverMovies(
    private val movies: MovieRepository
) {

    suspend operator fun invoke(suggestionData: SuggestionData) =
        invoke(suggestionData.actors, suggestionData.genres, suggestionData.years.randomOrNull())

    suspend operator fun invoke(
        actors: Collection<Actor> = emptySet(),
        genres: Collection<Genre> = emptySet(),
        years: FiveYearRange? = null
    ): Collection<Movie> = movies.discover(actors, genres, years)
}
