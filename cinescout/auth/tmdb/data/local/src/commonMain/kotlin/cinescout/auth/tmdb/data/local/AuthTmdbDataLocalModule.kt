package cinescout.auth.tmdb.data.local

import cinescout.auth.tmdb.data.TmdbAuthLocalDataSource
import org.koin.dsl.module

val AuthTmdbDataLocalModule = module {

    factory<TmdbAuthLocalDataSource> { RealTmdbAuthLocalDataSource(credentialsQueries = get()) }
}
