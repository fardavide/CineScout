package cinescout.auth.trakt.data.local

import cinescout.auth.trakt.data.TraktAuthLocalDataSource
import cinescout.network.trakt.TraktAuthProvider
import cinescout.utils.kotlin.DispatcherQualifier
import org.koin.dsl.module

val AuthTraktDataLocalModule = module {

    factory<TraktAuthLocalDataSource> {
        RealTraktAuthLocalDataSource(
            authStateQueries = get(),
            dispatcher = get(DispatcherQualifier.Io)
        )
    }
    single<TraktAuthProvider> { RealTraktAuthProvider(dataSource = get()) }
}
