package cinescout.auth.trakt.data.local

import cinescout.auth.trakt.data.TraktAuthLocalDataSource
import org.koin.dsl.module

val AuthTraktDataLocalModule = module {

    factory<TraktAuthLocalDataSource> { RealTraktAuthLocalDataSource(tokensQueries = get()) }
}
