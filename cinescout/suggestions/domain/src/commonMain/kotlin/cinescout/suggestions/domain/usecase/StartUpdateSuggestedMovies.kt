package cinescout.suggestions.domain.usecase

import cinescout.suggestions.domain.model.SuggestionsMode

interface StartUpdateSuggestedMovies {

    operator fun invoke(suggestionsMode: SuggestionsMode)
}
