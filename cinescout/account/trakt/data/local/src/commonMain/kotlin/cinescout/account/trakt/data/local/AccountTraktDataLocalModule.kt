package cinescout.account.trakt.data.local

import cinescout.account.trakt.data.TraktAccountLocalDataSource
import cinescout.account.trakt.data.local.mapper.TraktAccountMapper
import cinescout.utils.kotlin.DispatcherQualifier
import org.koin.dsl.module

val AccountTraktDataLocalModule = module {

    factory { TraktAccountMapper() }
    factory<TraktAccountLocalDataSource> {
        RealTraktAccountLocalDataSource(
            accountMapper = get(),
            accountQueries = get(),
            dispatcher = get(DispatcherQualifier.Io)
        )
    }
}
