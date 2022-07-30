package cinescout.account.tmdb.data

import cinescout.account.tmdb.domain.TmdbAccountRepository
import org.koin.dsl.module

val AccountTmdbDataModule = module {

    factory<TmdbAccountRepository> { RealTmdbAccountRepository(localDataSource = get(), remoteDataSource = get()) }
}
