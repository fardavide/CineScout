package cinescout.suggestions.domain.usecase

import arrow.core.Either
import cinescout.suggestions.domain.SuggestionRepository
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.utils.kotlin.mapToUnit
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
class UpdateSuggestedTvShows(
    private val generateSuggestedTvShows: GenerateSuggestedTvShows,
    private val suggestionRepository: SuggestionRepository
) {

    suspend operator fun invoke(suggestionsMode: SuggestionsMode): Either<SuggestionError, Unit> =
        generateSuggestedTvShows(suggestionsMode).first()
            .onRight { movies -> suggestionRepository.storeSuggestedTvShows(movies) }
            .mapToUnit()
}
