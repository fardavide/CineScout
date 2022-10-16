package cinescout.account.trakt.data.remote

import arrow.core.Either
import cinescout.account.domain.model.Gravatar
import cinescout.account.trakt.data.TraktAccountRemoteDataSource
import cinescout.account.trakt.domain.model.TraktAccount
import cinescout.account.trakt.domain.model.TraktAccountUsername
import cinescout.auth.trakt.domain.usecase.CallWithTraktAccount
import cinescout.model.NetworkOperation

class RealTraktAccountRemoteDataSource(
    private val callWithTraktAccount: CallWithTraktAccount,
    private val service: TraktAccountService
) : TraktAccountRemoteDataSource {

    override suspend fun getAccount(): Either<NetworkOperation, TraktAccount> =
        callWithTraktAccount {
            service.getAccount().map { response ->
                TraktAccount(
                    gravatar = Gravatar.fromUrl(response.user.images.avatar.full),
                    username = TraktAccountUsername(response.user.username)
                )
            }
        }
}
