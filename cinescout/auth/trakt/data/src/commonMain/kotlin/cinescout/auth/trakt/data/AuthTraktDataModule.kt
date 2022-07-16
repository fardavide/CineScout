package cinescout.auth.trakt.data

import cinescout.auth.trakt.domain.TraktAuthRepository
import cinescout.utils.kotlin.DispatcherQualifier
import org.koin.dsl.module

val AuthTraktDataModule = module {

    factory<TraktAuthRepository> {
        RealTraktAuthRepository(
            dispatcher = get(DispatcherQualifier.Io),
            localDataSource = get(),
            remoteDataSource = get()
        )
    }
}
