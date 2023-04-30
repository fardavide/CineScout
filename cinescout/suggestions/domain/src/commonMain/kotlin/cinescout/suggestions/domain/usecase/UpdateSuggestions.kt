package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.Nel
import arrow.core.continuations.either
import arrow.core.handleErrorWith
import arrow.core.left
import arrow.core.right
import cinescout.anticipated.domain.store.MostAnticipatedIdsStore
import cinescout.error.NetworkError
import cinescout.popular.domain.store.PopularIdsStore
import cinescout.recommended.domain.store.RecommendedIdsStore
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.store.PersonalRecommendedScreenplayIdsStore
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
        type: ScreenplayTypeFilter,
        suggestionsMode: SuggestionsMode
    ): Either<NetworkError, Unit>
}

@Factory
class RealUpdateSuggestions(
    private val anticipatedIdsStore: MostAnticipatedIdsStore,
    private val generateSuggestions: GenerateSuggestions,
    private val personalRecommendedScreenplayIdsStore: PersonalRecommendedScreenplayIdsStore,
    private val popularIdsStore: PopularIdsStore,
    private val recommendedIdsStore: RecommendedIdsStore,
    private val suggestionRepository: SuggestionRepository,
    private val trendingIdsStore: TrendingIdsStore
) : UpdateSuggestions {

    override suspend operator fun invoke(
        type: ScreenplayTypeFilter,
        suggestionsMode: SuggestionsMode
    ): Either<NetworkError, Unit> = coroutineScope {
        val anticipatedDeferred = async {
            anticipatedIdsStore.fresh(MostAnticipatedIdsStore.Key(type))
        }
        val generatedDeferred = async { generateSuggestions(type, suggestionsMode).first() }
        val personalRecommendedDeferred = async {
            personalRecommendedScreenplayIdsStore.freshAsOperation()
        }
        val popularDeferred = async { popularIdsStore.fresh(PopularIdsStore.Key(type)) }
        val recommendedDeferred = async { recommendedIdsStore.fresh(RecommendedIdsStore.Key(type)) }
        val trendingDeferred = async { trendingIdsStore.fresh(TrendingIdsStore.Key(type)) }

        either {
            val anticipated = anticipatedDeferred.await()
                .mapToSuggestedScreenplayId(SuggestionSource.Anticipated)
                .bind()
            val generated = generatedDeferred.await()
                .handleNoSuggestionsError()
                .bind()
            val personalRecommended = personalRecommendedDeferred.await()
                .handleSkippedAsEmpty()
                .mapToSuggestedScreenplayId(SuggestionSource.PersonalSuggestions)
                .bind()
            val popular = popularDeferred.await()
                .mapToSuggestedScreenplayId(SuggestionSource.Popular)
                .bind()
            val recommended = recommendedDeferred.await()
                .mapToSuggestedScreenplayId(SuggestionSource.Recommended)
                .bind()
            val trending = trendingDeferred.await()
                .mapToSuggestedScreenplayId(SuggestionSource.Trending)
                .bind()

            with(suggestionRepository) {
                storeSuggestionIds(anticipated + personalRecommended + popular + recommended + trending)
                storeSuggestions(generated)
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
        type: ScreenplayTypeFilter,
        suggestionsMode: SuggestionsMode
    ): Either<NetworkError, Unit> {
        invoked = true
        delay(delay)
        return error?.left() ?: Unit.right()
    }
}
