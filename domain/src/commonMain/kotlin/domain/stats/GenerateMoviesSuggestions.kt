package domain.stats

import co.touchlab.kermit.Logger
import domain.DiscoverMovies
import entities.Either
import entities.Error
import entities.movies.Movie
import entities.or
import entities.right
import entities.stats.StatRepository
import entities.suggestions.SuggestionData
import util.d

/**
 * Generate a [List] of suggested [Movie]s
 */
class GenerateMoviesSuggestions(
    private val discover: DiscoverMovies,
    private val generateDiscoverParams: GenerateDiscoverParams,
    private val getSuggestionsData: GetSuggestionData,
    private val stats: StatRepository,
    private val logger: Logger
) {

    suspend operator fun invoke(
        dataLimit: Int = DefaultLimit,
        includeRated: Boolean = false
    ): Either<NoRatedMovies, List<Movie>> = Either.fix {
        val limit = dataLimit.coerceAtLeast(GetSuggestedMovies.SuggestionsDivider)
        val (suggestionData) = getSuggestionsData(limit) or NoRatedMovies
        logger.d(suggestionData, "GenerateMoviesSuggestions")
        val (fromDiscover) = discover(generateDiscoverParams(suggestionData))
        return fromDiscover.let { collection ->
            if (includeRated) collection
            else collection.excludeRated()
        }.sortedByDescending { calculatePertinence(it, suggestionData) }.right()
    }

    private suspend fun Collection<Movie>.excludeRated(): List<Movie> {
        val ratedMovies = stats.ratedMovies()
        return filterNot { movie -> movie.id in ratedMovies.map { it.first.id } }
    }

    private fun calculatePertinence(movie: Movie, suggestionData: SuggestionData): Float {
        var wholePertinence = 0f
        wholePertinence += movie.actors.intersect(suggestionData.actors).size * ACTOR_PERTINENCE
        wholePertinence += movie.genres.intersect(suggestionData.genres).size * GENRE_PERTINENCE
        if (suggestionData.years.any { movie.year in it.range })
            wholePertinence += YEAR_PERTINENCE
        return wholePertinence / 100
    }

    private companion object {
        const val DefaultLimit = 5

        const val ACTOR_PERTINENCE = 10
        const val GENRE_PERTINENCE = 5
        const val YEAR_PERTINENCE = 7
    }
}

object NoRatedMovies : Error
