package cinescout.suggestions.domain.usecase

import arrow.core.Either
import cinescout.common.model.SuggestionError
import cinescout.movies.domain.MovieRepository
import cinescout.suggestions.domain.model.SuggestionsMode
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
class UpdateSuggestedMovies(
    private val generateSuggestedMovies: GenerateSuggestedMovies,
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(suggestionsMode: SuggestionsMode): Either<SuggestionError, Unit> =
        generateSuggestedMovies(suggestionsMode).first()
            .tap { movies -> movieRepository.storeSuggestedMovies(movies) }
            .void()
}
