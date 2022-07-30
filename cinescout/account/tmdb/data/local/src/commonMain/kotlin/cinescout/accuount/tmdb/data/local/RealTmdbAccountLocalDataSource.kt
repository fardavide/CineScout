package cinescout.accuount.tmdb.data.local

import app.cash.sqldelight.coroutines.asFlow
import arrow.core.Either
import cinescout.account.tmdb.data.TmdbAccountLocalDataSource
import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.accuount.tmdb.data.local.mapper.TmdbAccountMapper
import cinescout.database.TmdbAccountQueries
import cinescout.database.model.DatabaseTmdbAccountUsername
import cinescout.database.util.mapToOneOrError
import cinescout.error.DataError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RealTmdbAccountLocalDataSource(
    private val accountMapper: TmdbAccountMapper,
    private val accountQueries: TmdbAccountQueries,
    private val dispatcher: CoroutineDispatcher
) : TmdbAccountLocalDataSource {

    override fun findAccount(): Flow<Either<DataError.Local, TmdbAccount>> =
        accountQueries.find().asFlow().mapToOneOrError(dispatcher).map { either ->
            either.map(accountMapper::toTmdbAccount)
        }

    override suspend fun insert(account: TmdbAccount) {
        accountQueries.insertAccount(
            username = DatabaseTmdbAccountUsername(account.username.value),
        )
    }
}
