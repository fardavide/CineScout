package cinescout.auth.tmdb.data.local

import cinescout.auth.tmdb.data.TmdbAuthLocalDataSource
import cinescout.network.tmdb.TmdbAuthProvider
import org.koin.dsl.module

val AuthTmdbDataLocalModule = module {

    factory<TmdbAuthLocalDataSource> {
        RealTmdbAuthLocalDataSource(
            authStateQueries = get()
        )
    }
    single<TmdbAuthProvider> { RealTmdbAuthProvider(dataSource = get()) }
}
