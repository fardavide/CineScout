package cinescout.suggestions.domain.usecase

import cinescout.suggestions.domain.model.SuggestionsMode

fun interface StartUpdateSuggestions {

    operator fun invoke(suggestionsMode: SuggestionsMode)
}
