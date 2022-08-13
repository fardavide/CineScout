package cinescout.suggestions.domain.usecase

import cinescout.suggestions.domain.model.SuggestionsMode

fun interface StartUpdateSuggestedMovies {

    operator fun invoke(suggestionsMode: SuggestionsMode)
}
