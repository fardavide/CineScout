package cinescout.account.trakt.data.store

import cinescout.account.domain.model.Account
import cinescout.account.domain.store.AccountStore
import cinescout.account.trakt.data.TraktAccountLocalDataSource
import cinescout.account.trakt.data.TraktAccountRemoteDataSource
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [AccountStore::class])
internal class RealAccountStore(
    private val localDataSource: TraktAccountLocalDataSource,
    private val remoteDataSource: TraktAccountRemoteDataSource
) : AccountStore,
    Store5<Unit, Account> by Store5Builder.from<Unit, Account>(
        fetcher = EitherFetcher.ofOperation { remoteDataSource.getAccount() },
        sourceOfTruth = SourceOfTruth.Companion.of(
            reader = { localDataSource.findAccount() },
            writer = { _, account -> localDataSource.insert(account) },
            delete = { localDataSource.deleteAccount() },
            deleteAll = { localDataSource.deleteAccount() }
        )
    ).build()
