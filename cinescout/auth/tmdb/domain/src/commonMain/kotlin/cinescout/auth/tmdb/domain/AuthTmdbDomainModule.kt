package cinescout.auth.tmdb.domain

import cinescout.auth.tmdb.domain.usecase.IsTmdbLinked
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.auth.tmdb.domain.usecase.NotifyTmdbAppAuthorized
import org.koin.dsl.module

val AuthTmdbDomainModule = module {

    factory { IsTmdbLinked(tmdbAuthRepository = get()) }
    factory { LinkToTmdb(syncTmdbAccount = get(), tmdbAuthRepository = get()) }
    factory { NotifyTmdbAppAuthorized(authRepository = get()) }
}
