package cinescout.account.trakt.domain

import cinescout.account.trakt.domain.usecase.GetTraktAccount
import cinescout.account.trakt.domain.usecase.SyncTraktAccount
import org.koin.dsl.module

val AccountTraktDomainModule = module {
    factory { GetTraktAccount(accountRepository = get()) }
    factory { SyncTraktAccount(accountRepository = get()) }
}
