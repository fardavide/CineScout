package cinescout.auth.tmdb.domain

import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import org.koin.dsl.module

val AuthTmdbDomainModule = module {

    factory { LinkToTmdb(tmdbAuthRepository = get()) }
}
