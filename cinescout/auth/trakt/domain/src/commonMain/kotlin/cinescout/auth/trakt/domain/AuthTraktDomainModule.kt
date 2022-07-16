package cinescout.auth.trakt.domain

import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import org.koin.dsl.module

val AuthTraktDomainModule = module {

    factory { LinkToTrakt(traktAuthRepository = get()) }
}
