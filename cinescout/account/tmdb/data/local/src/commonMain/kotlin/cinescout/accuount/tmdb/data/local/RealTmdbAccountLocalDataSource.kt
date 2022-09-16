package cinescout.accuount.tmdb.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import cinescout.account.tmdb.data.TmdbAccountLocalDataSource
import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.accuount.tmdb.data.local.mapper.TmdbAccountMapper
import cinescout.database.TmdbAccountQueries
import cinescout.database.model.DatabaseGravatarHash
import cinescout.database.model.DatabaseTmdbAccountUsername
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RealTmdbAccountLocalDataSource(
    private val accountMapper: TmdbAccountMapper,
    private val accountQueries: TmdbAccountQueries,
    private val dispatcher: CoroutineDispatcher
) : TmdbAccountLocalDataSource {

    override fun findAccount(): Flow<TmdbAccount?> =
        accountQueries.find().asFlow().mapToOneOrNull(dispatcher).map { account ->
            account?.let(accountMapper::toTmdbAccount)
        }

    override suspend fun insert(account: TmdbAccount) {
        withContext(dispatcher) {
            accountQueries.insertAccount(
                gravatarHash = account.gravatar?.hash?.let(::DatabaseGravatarHash),
                username = DatabaseTmdbAccountUsername(account.username.value)
            )
        }
    }
}
