package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.Nel
import arrow.core.continuations.either
import arrow.core.handleErrorWith
import arrow.core.left
import arrow.core.right
import cinescout.anticipated.domain.store.MostAnticipatedIdsStore
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.store.RecommendedScreenplayIdsStore
import cinescout.store5.freshAsOperation
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionSource
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.repository.SuggestionRepository
import cinescout.trending.domain.store.TrendingIdsStore
import cinescout.utils.kotlin.handleSkippedAsEmpty
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory
import kotlin.time.Duration

interface UpdateSuggestions {

    suspend operator fun invoke(
        type: ScreenplayType,
        suggestionsMode: SuggestionsMode
    ): Either<NetworkError, Unit>
}

@Factory
class RealUpdateSuggestions(
    private val anticipatedIdsStore: MostAnticipatedIdsStore,
    private val generateSuggestions: GenerateSuggestions,
    private val recommendedScreenplayIdsStore: RecommendedScreenplayIdsStore,
    private val suggestionRepository: SuggestionRepository,
    private val trendingIdsStore: TrendingIdsStore
) : UpdateSuggestions {

    override suspend operator fun invoke(
        type: ScreenplayType,
        suggestionsMode: SuggestionsMode
    ): Either<NetworkError, Unit> = coroutineScope {
        val anticipatedDeferred = async {
            anticipatedIdsStore.fresh(MostAnticipatedIdsStore.Key(type))
        }
        val generatedDeferred = async { generateSuggestions(type, suggestionsMode).first() }
        val recommendedDeferred = async { recommendedScreenplayIdsStore.freshAsOperation() }
        val trendingDeferred = async { trendingIdsStore.fresh(TrendingIdsStore.Key(type)) }

        either {
            val anticipated = anticipatedDeferred.await()
                .mapToSuggestedScreenplayId(SuggestionSource.Anticipated)
                .bind()
            val generated = generatedDeferred.await()
                .handleNoSuggestionsError()
                .bind()
            val recommended = recommendedDeferred.await()
                .handleSkippedAsEmpty()
                .mapToSuggestedScreenplayId(SuggestionSource.PersonalSuggestions)
                .bind()
            val trending = trendingDeferred.await()
                .mapToSuggestedScreenplayId(SuggestionSource.Trending)
                .bind()

            with(suggestionRepository) {
                storeSuggestionIds(anticipated)
                storeSuggestionIds(recommended)
                storeSuggestions(generated)
                storeSuggestionIds(trending)
            }
        }
    }

    private fun Either<NetworkError, List<ScreenplayIds>>.mapToSuggestedScreenplayId(
        source: SuggestionSource
    ) = map { list -> list.map { ids -> SuggestedScreenplayId(ids, source) } }

    private fun <T> Either<SuggestionError, Nel<T>>.handleNoSuggestionsError() = handleErrorWith { error ->
        when (error) {
            is SuggestionError.NoSuggestions -> emptyList<T>().right()
            is SuggestionError.Source -> error.networkError.left()
        }
    }
}

class FakeUpdateSuggestions(
    private val delay: Duration = Duration.ZERO,
    var error: NetworkError? = null
) : UpdateSuggestions {

    var invoked: Boolean = false
        private set

    override suspend operator fun invoke(
        type: ScreenplayType,
        suggestionsMode: SuggestionsMode
    ): Either<NetworkError, Unit> {
        invoked = true
        delay(delay)
        return error?.left() ?: Unit.right()
    }
}
