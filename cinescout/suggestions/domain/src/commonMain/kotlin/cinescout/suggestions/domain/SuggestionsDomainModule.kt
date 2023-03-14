package cinescout.suggestions.domain

import cinescout.suggestions.domain.usecase.GetSuggestionIds
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named

@Module
@ComponentScan
class SuggestionsDomainModule {

    @Factory
    @Named(GetSuggestionIds.UpdateIfSuggestionsLessThanName)
    fun updateTvShowsIfSuggestionsLessThan() = GetSuggestionIds.DefaultMinimumSuggestions
}
