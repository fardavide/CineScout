package cinescout.account.trakt.data

import cinescout.account.trakt.domain.TraktAccountRepository
import org.koin.dsl.module

val AccountTraktDataModule = module {
    factory<TraktAccountRepository> { RealTraktAccountRepository(localDataSource = get(), remoteDataSource = get()) }
}
