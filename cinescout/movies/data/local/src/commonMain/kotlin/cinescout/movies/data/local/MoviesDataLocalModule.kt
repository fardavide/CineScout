package cinescout.movies.data.local

import cinescout.movies.data.LocalMovieDataSource
import org.koin.dsl.module

val MoviesDataLocalModule = module {

    factory<LocalMovieDataSource> { MockLocalMovieDataSource() }
}
