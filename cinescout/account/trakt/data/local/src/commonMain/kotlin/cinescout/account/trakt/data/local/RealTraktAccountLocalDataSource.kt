package cinescout.account.trakt.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneNotNull
import app.cash.sqldelight.coroutines.mapToOneOrNull
import arrow.core.Either
import cinescout.account.trakt.data.TraktAccountLocalDataSource
import cinescout.account.trakt.data.local.mapper.TraktAccountMapper
import cinescout.account.trakt.domain.model.TraktAccount
import cinescout.database.TraktAccountQueries
import cinescout.database.model.DatabaseGravatarHash
import cinescout.database.model.DatabaseTraktAccountUsername
import cinescout.database.util.mapToOneOrError
import cinescout.error.DataError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RealTraktAccountLocalDataSource(
    private val accountMapper: TraktAccountMapper,
    private val accountQueries: TraktAccountQueries,
    private val dispatcher: CoroutineDispatcher
) : TraktAccountLocalDataSource {

    override fun findAccount(): Flow<TraktAccount?> =
        accountQueries.find().asFlow().mapToOneOrNull(dispatcher).map { account ->
            account?.let(accountMapper::toTraktAccount)
        }

    override suspend fun insert(account: TraktAccount) {
        accountQueries.insertAccount(
            gravatarHash = account.gravatar?.hash?.let(::DatabaseGravatarHash),
            username = DatabaseTraktAccountUsername(account.username.value),
        )
    }
}
