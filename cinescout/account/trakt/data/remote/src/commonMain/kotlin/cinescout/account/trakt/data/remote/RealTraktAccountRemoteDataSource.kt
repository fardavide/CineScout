package cinescout.account.trakt.data.remote

import arrow.core.Either
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.AccountUsername
import cinescout.account.domain.model.Gravatar
import cinescout.account.trakt.data.TraktAccountRemoteDataSource
import cinescout.auth.trakt.domain.usecase.CallWithTraktAccount
import cinescout.model.NetworkOperation
import org.koin.core.annotation.Factory

@Factory
class RealTraktAccountRemoteDataSource(
    private val callWithTraktAccount: CallWithTraktAccount,
    private val service: TraktAccountService
) : TraktAccountRemoteDataSource {

    override suspend fun getAccount(): Either<NetworkOperation, Account.Trakt> = callWithTraktAccount {
        service.getAccount().map { response ->
            Account.Trakt(
                gravatar = Gravatar.fromUrl(response.user.images.avatar.full),
                username = AccountUsername(response.user.username)
            )
        }
    }
}
