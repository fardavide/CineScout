package cinescout.suggestions.domain.usecase

import arrow.core.Either
import cinescout.common.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.tvshows.domain.TvShowRepository
import cinescout.utils.kotlin.mapToUnit
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
class UpdateSuggestedTvShows(
    private val generateSuggestedTvShows: GenerateSuggestedTvShows,
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(suggestionsMode: SuggestionsMode): Either<SuggestionError, Unit> =
        generateSuggestedTvShows(suggestionsMode).first()
            .onRight { movies -> tvShowRepository.storeSuggestedTvShows(movies) }
            .mapToUnit()
}
