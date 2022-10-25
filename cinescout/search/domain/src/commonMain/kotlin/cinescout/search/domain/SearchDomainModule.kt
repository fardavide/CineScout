package cinescout.search.domain

import cinescout.search.domain.usecase.SearchMovies
import cinescout.search.domain.usecase.SearchTvShows
import org.koin.dsl.module

val SearchDomainModule = module {

    factory { SearchMovies(movieRepository = get()) }
    factory { SearchTvShows(tvShowRepository = get()) }
}
