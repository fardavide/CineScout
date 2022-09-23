package cinescout.search.domain

import cinescout.search.domain.usecase.SearchMovies
import org.koin.dsl.module

val SearchDomainModule = module {

    factory { SearchMovies(movieRepository = get()) }
}
