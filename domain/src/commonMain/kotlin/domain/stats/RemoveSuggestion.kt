package domain.stats

import domain.DiscoverMovies
import entities.Either
import entities.ResourceError
import entities.movies.Movie
import entities.stats.StatRepository
import entities.suggestions.SuggestionData
import kotlinx.coroutines.flow.Flow

class RemoveSuggestion(
    private val stats: StatRepository,
) {

    suspend operator fun invoke(movie: Movie) {
        stats.removeSuggestion(movie)
    }
}
