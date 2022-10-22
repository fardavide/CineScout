package cinescout.suggestions.domain.usecase

import arrow.core.Either
import cinescout.common.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.tvshows.domain.TvShowRepository
import kotlinx.coroutines.flow.first

class UpdateSuggestedTvShows(
    private val generateSuggestedTvShows: GenerateSuggestedTvShows,
    private val movieRepository: TvShowRepository
) {

    suspend operator fun invoke(suggestionsMode: SuggestionsMode): Either<SuggestionError, Unit> =
        generateSuggestedTvShows(suggestionsMode).first()
            .tap { movies -> movieRepository.storeSuggestedTvShows(movies) }
            .void()
}
