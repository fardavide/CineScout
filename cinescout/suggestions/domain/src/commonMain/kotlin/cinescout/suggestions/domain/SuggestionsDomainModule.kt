package cinescout.suggestions.domain

import cinescout.suggestions.domain.usecase.GetSuggestedMovie
import org.koin.dsl.module

val SuggestionsDomainModule = module {

    factory { GetSuggestedMovie() }
}
