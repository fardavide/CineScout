package cinescout.suggestions.domain

import cinescout.suggestions.domain.usecase.BuildDiscoverMoviesParams
import cinescout.suggestions.domain.usecase.GetSuggestedMovie
import org.koin.dsl.module

val SuggestionsDomainModule = module {

    factory { BuildDiscoverMoviesParams(getAllRatedMovies = get()) }
    factory { GetSuggestedMovie() }
}
