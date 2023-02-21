package cinescout.account.tmdb.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import cinescout.account.domain.model.Account
import cinescout.account.tmdb.data.TmdbAccountLocalDataSource
import cinescout.account.tmdb.data.local.mapper.TmdbAccountMapper
import cinescout.database.TmdbAccountQueries
import cinescout.database.model.DatabaseGravatarHash
import cinescout.database.model.DatabaseTmdbAccountUsername
import cinescout.utils.kotlin.DispatcherQualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
class RealTmdbAccountLocalDataSource(
    private val accountMapper: TmdbAccountMapper,
    private val accountQueries: TmdbAccountQueries,
    @Named(DispatcherQualifier.Io) private val dispatcher: CoroutineDispatcher,
    @Named(DispatcherQualifier.DatabaseWrite) private val writeDispatcher: CoroutineDispatcher
) : TmdbAccountLocalDataSource {

    override suspend fun deleteAccount() {
        withContext(writeDispatcher) {
            accountQueries.delete()
        }
    }

    override fun findAccount(): Flow<Account.Tmdb?> =
        accountQueries.find().asFlow().mapToOneOrNull(dispatcher).map { account ->
            account?.let(accountMapper::toTmdbAccount)
        }

    override suspend fun insert(account: Account.Tmdb) {
        withContext(writeDispatcher) {
            accountQueries.insertAccount(
                gravatarHash = account.gravatar?.hash?.let(::DatabaseGravatarHash),
                username = DatabaseTmdbAccountUsername(account.username.value)
            )
        }
    }
}
