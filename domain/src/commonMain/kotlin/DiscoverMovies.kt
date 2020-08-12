import movies.MovieRepository
import suggestions.SuggestionData

class DiscoverMovies(
    private val movies: MovieRepository
) {

    suspend operator fun invoke(suggestionData: SuggestionData) =
        invoke(suggestionData.actors, suggestionData.genres, suggestionData.years.randomOrNull())

    suspend operator fun invoke(
        actors: Collection<Name> = emptySet(),
        genres: Collection<Name> = emptySet(),
        years: FiveYearRange? = null
    ) = movies.discover(actors, genres, years)
}
