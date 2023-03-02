package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.common.model.SuggestionError
import cinescout.movies.domain.MovieRepository
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.utils.kotlin.mapToUnit
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory
import kotlin.time.Duration

interface UpdateSuggestedMovies {

    suspend operator fun invoke(suggestionsMode: SuggestionsMode): Either<SuggestionError, Unit>
}

@Factory
class RealUpdateSuggestedMovies(
    private val generateSuggestedMovies: GenerateSuggestedMovies,
    private val movieRepository: MovieRepository
) : UpdateSuggestedMovies {

    override suspend operator fun invoke(suggestionsMode: SuggestionsMode): Either<SuggestionError, Unit> =
        generateSuggestedMovies(suggestionsMode).first()
            .onRight { movies -> movieRepository.storeSuggestedMovies(movies) }
            .mapToUnit()
}

class FakeUpdateSuggestedMovies(
    private val delay: Duration = Duration.ZERO,
    private val error: SuggestionError? = null
) : UpdateSuggestedMovies {

    var invoked: Boolean = false
        private set

    override suspend operator fun invoke(suggestionsMode: SuggestionsMode): Either<SuggestionError, Unit> {
        invoked = true
        delay(delay)
        return error?.left() ?: Unit.right()
    }
}
