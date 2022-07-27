package cinescout.auth.tmdb.data.local

import cinescout.auth.tmdb.data.TmdbAuthLocalDataSource
import cinescout.network.tmdb.TmdbAuthProvider
import cinescout.utils.kotlin.DispatcherQualifier
import org.koin.dsl.module

val AuthTmdbDataLocalModule = module {

    factory<TmdbAuthLocalDataSource> {
        RealTmdbAuthLocalDataSource(
            authStateQueries = get(),
            dispatcher = get(DispatcherQualifier.Io)
        )
    }
    single<TmdbAuthProvider> { RealTmdbAuthProvider(dataSource = get()) }
}
