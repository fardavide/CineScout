package cinescout.accuount.tmdb.data.local

import cinescout.account.tmdb.data.TmdbAccountLocalDataSource
import cinescout.accuount.tmdb.data.local.mapper.TmdbAccountMapper
import cinescout.utils.kotlin.DispatcherQualifier
import org.koin.dsl.module

val AccountTmdbDataLocalModule = module {

    factory { TmdbAccountMapper() }
    factory<TmdbAccountLocalDataSource> {
        RealTmdbAccountLocalDataSource(
            accountMapper = get(),
            accountQueries = get(),
            dispatcher = get(DispatcherQualifier.Io)
        )
    }
}
