package cinescout.account.tmdb.domain

import cinescout.account.tmdb.domain.usecase.GetTmdbAccount
import org.koin.dsl.module

val AccountTmdbDomainModule = module {

    factory { GetTmdbAccount(accountRepository = get()) }
}
