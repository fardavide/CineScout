package domain.stats

import co.touchlab.kermit.Logger
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
    private val generateMoviesSuggestions: GenerateMoviesSuggestions,
    private val logger: Logger
) {

    private var lastLoadTimestamp = DateTime.EPOCH

    operator fun invoke(): Flow<Either<ResourceError, Collection<Movie>>> =
        stats.suggestions()
            .onEach { either ->
                val storedSuggestionsCount = either.rightOrNull()?.size ?: 0
                if (storedSuggestionsCount < StatRepository.STORED_SUGGESTIONS_LIMIT)
                    loadMoreSuggestions(dataLimit = storedSuggestionsCount / SuggestionsDivider)
            }.distinctUntilChanged()

    private suspend fun loadMoreSuggestions(dataLimit: Int) {
        if (DateTime.now() < lastLoadTimestamp + 5.seconds)
            return

        coroutineScope {
            launch {
                val suggestionsEither = generateMoviesSuggestions(dataLimit)
                if (suggestionsEither.isRight()) {
                    stats.addSuggestions(suggestionsEither.rightOrThrow())
                } else {
                    logger.i("No rated movies, skipping", "GetSuggestedMovies")
                }

                lastLoadTimestamp = DateTime.now()
            }
        }
    }

    private companion object {

        /**
         * Data limit for [GenerateMoviesSuggestions] over store suggestions
         * I.E. `dataLimit = storeSuggestionsCount / [SuggestionsDivider]`
         */
        const val SuggestionsDivider = 5
    }
}
