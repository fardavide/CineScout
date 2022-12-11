package cinescout.suggestions.domain

import cinescout.suggestions.domain.usecase.GetSuggestedMovies
import cinescout.suggestions.domain.usecase.GetSuggestedTvShows
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named

@Module
@ComponentScan
class SuggestionsDomainModule {

    @Factory
    @Named(GetSuggestedMovies.UpdateIfSuggestionsLessThanName)
    fun updateMoviesIfSuggestionsLessThan() = GetSuggestedMovies.DefaultMinimumSuggestions

    @Factory
    @Named(GetSuggestedTvShows.UpdateIfSuggestionsLessThanName)
    fun updateTvShowsIfSuggestionsLessThan() = GetSuggestedTvShows.DefaultMinimumSuggestions
}
