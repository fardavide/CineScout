package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.left
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionsMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class GetSuggestedMovies(
    private val movieRepository: MovieRepository,
    private val updateSuggestedMovies: UpdateSuggestedMovies,
    private val updateIfSuggestionsLessThan: Int = DefaultMinimumSuggestions
) {

    operator fun invoke(): Flow<Either<SuggestionError, NonEmptyList<Movie>>> =
        movieRepository.getSuggestedMovies().transform { either ->
            either
                .tap { movies ->
                    emit(movies.right())
                    if (movies.size < updateIfSuggestionsLessThan) {
                        updateSuggestedMovies(SuggestionsMode.Quick)
                    }
                }
                .tapLeft {
                    updateSuggestedMovies(SuggestionsMode.Quick)
                        .tapLeft { error -> emit(error.left()) }
                }
        }

    companion object {

        const val DefaultMinimumSuggestions = 10
    }
}
