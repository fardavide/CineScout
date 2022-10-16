package cinescout.auth.trakt.domain

import cinescout.auth.trakt.domain.usecase.CallWithTraktAccount
import cinescout.auth.trakt.domain.usecase.IsTraktLinked
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.auth.trakt.domain.usecase.NotifyTraktAppAuthorized
import org.koin.dsl.module

val AuthTraktDomainModule = module {

    single { CallWithTraktAccount(appScope = get(), isTraktLinked = get()) }
    factory { IsTraktLinked(traktAuthRepository = get()) }
    factory {
        LinkToTrakt(
            syncTraktAccount = get(),
            traktAuthRepository = get()
        )
    }
    factory { NotifyTraktAppAuthorized(authRepository = get()) }
}
