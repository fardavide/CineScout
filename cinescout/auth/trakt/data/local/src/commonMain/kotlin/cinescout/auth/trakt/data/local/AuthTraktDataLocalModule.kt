package cinescout.auth.trakt.data.local

import cinescout.auth.trakt.data.TraktAuthLocalDataSource
import cinescout.network.trakt.TraktAuthProvider
import org.koin.dsl.module

val AuthTraktDataLocalModule = module {

    factory<TraktAuthLocalDataSource> { RealTraktAuthLocalDataSource(tokensQueries = get()) }
    single<TraktAuthProvider> { RealTraktAuthProvider(dataSource = get()) }
}
