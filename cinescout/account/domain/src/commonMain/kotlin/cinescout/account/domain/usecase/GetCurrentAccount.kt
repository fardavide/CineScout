package cinescout.account.domain.usecase

import arrow.core.Either
import arrow.core.handleErrorWith
import arrow.core.left
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Factory

@Factory
class GetCurrentAccount(
    private val getTmdbAccount: GetTmdbAccount,
    private val getTraktAccount: GetTraktAccount
) {

    operator fun invoke(): Flow<Either<GetAccountError, Account>> = combine(
        getTmdbAccount(),
        getTraktAccount()
    ) { tmdbAccountEither, traktAccountEither ->
        check(tmdbAccountEither.isLeft() || traktAccountEither.isLeft()) {
            "Both accounts are connected: this is not supported"
        }
        tmdbAccountEither.handleErrorWith { tmdbError ->
            traktAccountEither.handleErrorWith { traktError ->
                if (tmdbError is GetAccountError.Network) {
                    tmdbError.left()
                } else {
                    traktError.left()
                }
            }
        }
    }
}
