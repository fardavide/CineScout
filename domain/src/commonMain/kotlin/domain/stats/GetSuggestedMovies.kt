package domain.stats

import domain.DiscoverMovies
import entities.Either
import entities.ResourceError
import entities.movies.Movie
import entities.stats.StatRepository
import entities.suggestions.SuggestionData
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

/**
 * Get a [List] of suggested [Movie]s
 * This fetches suggestions from the local database and, if needed, generates and stores new ones via
 *  [GenerateMoviesSuggestions]
 */
class GetSuggestedMovies(
    private val stats: StatRepository,
    private val generateMoviesSuggestions: GenerateMoviesSuggestions
) {

    operator fun invoke(): Flow<Either<ResourceError, Collection<Movie>>> =
        stats.suggestions()
            .onEach { either ->
                if (either is Either.Left || either.rightOrThrow().size < StatRepository.STORED_SUGGESTIONS_LIMIT)
                    loadMoreSuggestions()
            }.distinctUntilChanged()

    private suspend fun loadMoreSuggestions() {
        coroutineScope {
            launch {
                val suggestions = generateMoviesSuggestions()
                stats.addSuggestions(suggestions)
            }
        }
    }
}
