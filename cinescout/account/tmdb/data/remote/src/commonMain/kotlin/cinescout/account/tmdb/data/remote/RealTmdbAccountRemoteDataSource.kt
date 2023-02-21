package cinescout.account.tmdb.data.remote

import arrow.core.Either
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.AccountUsername
import cinescout.account.domain.model.Gravatar
import cinescout.account.tmdb.data.TmdbAccountRemoteDataSource
import cinescout.auth.tmdb.domain.usecase.CallWithTmdbAccount
import cinescout.model.NetworkOperation
import org.koin.core.annotation.Factory

@Factory
class RealTmdbAccountRemoteDataSource(
    private val callWithTmdbAccount: CallWithTmdbAccount,
    private val service: TmdbAccountService
) : TmdbAccountRemoteDataSource {

    override suspend fun getAccount(): Either<NetworkOperation, Account.Tmdb> = callWithTmdbAccount {
        service.getAccount().map { response ->
            Account.Tmdb(
                gravatar = response.avatar?.gravatar?.hash?.let(::Gravatar),
                username = AccountUsername(response.username)
            )
        }
    }
}
