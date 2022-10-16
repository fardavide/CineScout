package cinescout.account.trakt.data.remote

import cinescout.account.trakt.data.TraktAccountRemoteDataSource
import cinescout.network.trakt.TraktNetworkQualifier
import org.koin.dsl.module

val AccountTraktDataRemoteModule = module {
    factory<TraktAccountRemoteDataSource> {
        RealTraktAccountRemoteDataSource(
            callWithTraktAccount = get(),
            service = get()
        )
    }
    factory { TraktAccountService(client = get(TraktNetworkQualifier.Client)) }
}
