package cinescout.account.tmdb.domain

import cinescout.account.tmdb.domain.usecase.GetTmdbAccount
import cinescout.account.tmdb.domain.usecase.SyncTmdbAccount
import org.koin.dsl.module

val AccountTmdbDomainModule = module {

    factory { GetTmdbAccount(accountRepository = get()) }
    factory { SyncTmdbAccount(accountRepository = get()) }
}
