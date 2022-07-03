package cinescout.movies.domain

import cinescout.movies.domain.usecase.AddMovieToWatchlist
import org.koin.dsl.module

val MoviesDomainModule = module {

    factory { AddMovieToWatchlist() }
}
