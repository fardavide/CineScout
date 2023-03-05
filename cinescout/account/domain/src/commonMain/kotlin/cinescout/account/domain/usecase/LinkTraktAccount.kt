package cinescout.account.domain.usecase

import arrow.core.Either
import arrow.core.right
import cinescout.account.domain.AccountRepository
import cinescout.auth.domain.usecase.LinkToTrakt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Factory

interface LinkTraktAccount {

    operator fun invoke(): Flow<Either<LinkToTrakt.Error, LinkToTrakt.State>>
}

@Factory
class RealLinkTraktAccount(
    private val accountRepository: AccountRepository,
    private val linkToTrakt: LinkToTrakt
) : LinkTraktAccount {

    override operator fun invoke(): Flow<Either<LinkToTrakt.Error, LinkToTrakt.State>> =
        linkToTrakt().onEach { either ->
            either.onRight { state ->
                if (state is LinkToTrakt.State.Success) {
                    accountRepository.syncAccount()
                }
            }
        }
}

class FakeLinkTraktAccount(
    state: LinkToTrakt.State = LinkToTrakt.State.Success,
    private val result: Either<LinkToTrakt.Error, LinkToTrakt.State> = state.right()
) : LinkTraktAccount {

    var invoked = false
        private set

    override operator fun invoke(): Flow<Either<LinkToTrakt.Error, LinkToTrakt.State>> {
        invoked = true
        return flowOf(result)
    }
}
