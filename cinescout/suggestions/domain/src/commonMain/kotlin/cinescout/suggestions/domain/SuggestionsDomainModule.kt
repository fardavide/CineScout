package cinescout.suggestions.domain

import cinescout.suggestions.domain.usecase.GetSuggestedMovieIds
import cinescout.suggestions.domain.usecase.GetSuggestedTvShows
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named

@Module
@ComponentScan
class SuggestionsDomainModule {

    @Factory
    @Named(GetSuggestedMovieIds.UpdateIfSuggestionsLessThanName)
    fun updateMoviesIfSuggestionsLessThan() = GetSuggestedMovieIds.DefaultMinimumSuggestions

    @Factory
    @Named(GetSuggestedTvShows.UpdateIfSuggestionsLessThanName)
    fun updateTvShowsIfSuggestionsLessThan() = GetSuggestedTvShows.DefaultMinimumSuggestions
}
