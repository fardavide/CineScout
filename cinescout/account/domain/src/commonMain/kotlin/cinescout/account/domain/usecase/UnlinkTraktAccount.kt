package cinescout.account.domain.usecase

import cinescout.account.domain.AccountRepository
import cinescout.auth.domain.usecase.UnlinkFromTrakt
import org.koin.core.annotation.Factory

interface UnlinkTraktAccount {

    suspend operator fun invoke()
}

@Factory
class RealUnlinkTraktAccount(
    private val accountRepository: AccountRepository,
    private val unlinkFromTrakt: UnlinkFromTrakt
) : UnlinkTraktAccount {

    override suspend operator fun invoke() {
        unlinkFromTrakt()
        accountRepository.removeAccount()
    }
}

class FakeUnlinkTraktAccount : UnlinkTraktAccount {

    var invoked = false
        private set

    override suspend operator fun invoke() {
        invoked = true
    }
}
