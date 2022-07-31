package cinescout.account.tmdb.data.remote

import arrow.core.Either
import cinescout.account.tmdb.data.TmdbAccountRemoteDataSource
import cinescout.account.tmdb.domain.model.Gravatar
import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.account.tmdb.domain.model.TmdbAccountUsername
import cinescout.error.NetworkError

class RealTmdbAccountRemoteDataSource(
    private val service: TmdbAccountService
) : TmdbAccountRemoteDataSource {

    override suspend fun getAccount(): Either<NetworkError, TmdbAccount> =
        service.getAccount().map { response ->
            TmdbAccount(
                gravatar = response.avatar?.gravatar?.hash?.let(::Gravatar),
                username = TmdbAccountUsername(response.username)
            )
        }
}
