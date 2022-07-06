package cinescout.auth.tmdb.data

import cinescout.auth.tmdb.domain.TmdbAuthRepository
import cinescout.utils.kotlin.DispatcherQualifier
import org.koin.dsl.module

val AuthTmdbDataModule = module {

    factory<TmdbAuthRepository> {
        RealTmdbAuthRepository(
            dispatcher = get(DispatcherQualifier.Io),
            localDataSource = get(),
            remoteDataSource = get()
        )
    }
}
