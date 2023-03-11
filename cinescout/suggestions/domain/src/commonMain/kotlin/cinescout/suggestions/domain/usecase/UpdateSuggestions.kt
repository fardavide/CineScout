package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.Nel
import arrow.core.continuations.either
import arrow.core.handleErrorWith
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.screenplay.domain.store.RecommendedScreenplayIdsStore
import cinescout.store5.fresh
import cinescout.suggestions.domain.SuggestionRepository
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionSource
import cinescout.suggestions.domain.model.SuggestionsMode
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory
import kotlin.time.Duration

interface UpdateSuggestions {

    suspend operator fun invoke(suggestionsMode: SuggestionsMode): Either<DataError.Remote, Unit>
}

@Factory
class RealUpdateSuggestions(
    private val generateSuggestedMovies: GenerateSuggestedMovies,
    private val generateSuggestedTvShows: GenerateSuggestedTvShows,
    private val recommendedScreenplayIdsStore: RecommendedScreenplayIdsStore,
    private val suggestionRepository: SuggestionRepository
) : UpdateSuggestions {

    override suspend operator fun invoke(suggestionsMode: SuggestionsMode): Either<DataError.Remote, Unit> =
        coroutineScope {
            val generatedMoviesDeferred = async { generateSuggestedMovies(suggestionsMode).first() }
            val generatedTvShowsDeferred = async { generateSuggestedTvShows(suggestionsMode).first() }
            val recommendedDeferred = async { recommendedScreenplayIdsStore.fresh() }

            either {
                val generatedMovies = generatedMoviesDeferred.await()
                    .handleNoSuggestionsError()
                    .bind()
                val generatedTvShows = generatedTvShowsDeferred.await()
                    .handleNoSuggestionsError()
                    .bind()
                val recommended = recommendedDeferred.await()
                    .map { list -> list.map { SuggestedScreenplayId(it, SuggestionSource.PersonalSuggestions) } }
                    .mapLeft(DataError::Remote)
                    .bind()

                suggestionRepository.storeSuggestionIds(recommended)
                suggestionRepository.storeSuggestedMovies(generatedMovies)
                suggestionRepository.storeSuggestedTvShows(generatedTvShows)
            }
        }

    private fun <T> Either<SuggestionError, Nel<T>>.handleNoSuggestionsError() = handleErrorWith { error ->
        when (error) {
            is SuggestionError.NoSuggestions -> emptyList<T>().right()
            is SuggestionError.Source -> error.dataError.left()
        }
    }
}

class FakeUpdateSuggestions(
    private val delay: Duration = Duration.ZERO,
    private val error: DataError.Remote? = null
) : UpdateSuggestions {

    var invoked: Boolean = false
        private set

    override suspend operator fun invoke(suggestionsMode: SuggestionsMode): Either<DataError.Remote, Unit> {
        invoked = true
        delay(delay)
        return error?.left() ?: Unit.right()
    }
}
