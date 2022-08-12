package cinescout.suggestions.domain.usecase

import arrow.core.Either
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionsMode
import kotlinx.coroutines.flow.first

class UpdateSuggestedMovies(
    private val generateSuggestedMovies: GenerateSuggestedMovies,
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(suggestionsMode: SuggestionsMode): Either<SuggestionError, Unit> {
        return generateSuggestedMovies(suggestionsMode).first()
            .tap { movies -> movieRepository.storeSuggestedMovies(movies) }
            .void()
    }
}
