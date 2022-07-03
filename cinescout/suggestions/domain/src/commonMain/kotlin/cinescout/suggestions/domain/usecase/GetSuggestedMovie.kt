package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.left
import cinescout.movies.domain.model.Movie
import cinescout.suggestions.domain.model.NoSuggestions

class GetSuggestedMovie {

    suspend operator fun invoke(): Either<NoSuggestions, Movie> =
        NoSuggestions.left()
}
