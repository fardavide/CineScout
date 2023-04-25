package cinescout.account.trakt.data.local.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import cinescout.account.domain.model.Account
import cinescout.account.trakt.data.datasource.TraktAccountLocalDataSource
import cinescout.account.trakt.data.local.mapper.TraktAccountMapper
import cinescout.database.TraktAccountQueries
import cinescout.database.model.DatabaseGravatarHash
import cinescout.database.model.DatabaseTraktAccountUsername
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
class RealTraktAccountLocalDataSource(
    private val accountMapper: TraktAccountMapper,
    private val accountQueries: TraktAccountQueries,
    @Named(IoDispatcher) private val dispatcher: CoroutineDispatcher,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : TraktAccountLocalDataSource {

    override suspend fun deleteAccount() {
        withContext(writeDispatcher) {
            accountQueries.delete()
        }
    }

    override fun findAccount(): Flow<Account?> =
        accountQueries.find().asFlow().mapToOneOrNull(dispatcher).map { account ->
            account?.let(accountMapper::toTraktAccount)
        }

    override suspend fun insert(account: Account) {
        withContext(writeDispatcher) {
            accountQueries.insertAccount(
                gravatarHash = account.gravatar?.hash?.let(::DatabaseGravatarHash),
                username = DatabaseTraktAccountUsername(account.username.value)
            )
        }
    }
}
