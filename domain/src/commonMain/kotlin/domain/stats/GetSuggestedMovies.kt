package domain.stats

import com.soywiz.klock.DateTime
import com.soywiz.klock.seconds
import entities.Either
import entities.ResourceError
import entities.movies.Movie
import entities.stats.StatRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Get a [List] of suggested [Movie]s
 * This fetches suggestions from the local database and, if needed, generates and stores new ones via
 *  [GenerateMoviesSuggestions]
 */
class GetSuggestedMovies(
    private val stats: StatRepository,
    private val generateMoviesSuggestions: GenerateMoviesSuggestions
) {

    private var lastLoadTimestamp = DateTime.EPOCH

    operator fun invoke(): Flow<Either<ResourceError, Collection<Movie>>> =
        stats.suggestions()
            .onEach { either ->
                val storedSuggestionsCount = either.rightOrNull()?.size ?: 0
                if (storedSuggestionsCount < StatRepository.STORED_SUGGESTIONS_LIMIT)
                    loadMoreSuggestions()
            }.distinctUntilChanged()

    private suspend fun loadMoreSuggestions() {
        if (DateTime.now() > lastLoadTimestamp + 5.seconds)

        coroutineScope {
            launch {
                val suggestions = generateMoviesSuggestions()
                stats.addSuggestions(suggestions)

                lastLoadTimestamp = DateTime.now()
            }
        }
    }
}
