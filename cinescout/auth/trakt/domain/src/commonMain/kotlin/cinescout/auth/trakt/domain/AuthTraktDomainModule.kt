package cinescout.auth.trakt.domain

import cinescout.auth.trakt.domain.usecase.IsTraktLinked
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.auth.trakt.domain.usecase.NotifyTraktAppAuthorized
import org.koin.dsl.module

val AuthTraktDomainModule = module {

    factory { IsTraktLinked(traktAuthRepository = get()) }
    factory { NotifyTraktAppAuthorized(authRepository = get()) }
    factory { LinkToTrakt(syncRatedMovies = get(), syncTraktAccount = get(), traktAuthRepository = get()) }
}
