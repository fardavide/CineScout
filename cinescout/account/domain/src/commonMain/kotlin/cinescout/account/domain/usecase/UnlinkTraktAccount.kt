package cinescout.account.domain.usecase

import cinescout.account.domain.store.AccountStore
import cinescout.auth.domain.usecase.UnlinkFromTrakt
import org.koin.core.annotation.Factory

interface UnlinkTraktAccount {

    suspend operator fun invoke()
}

@Factory
class RealUnlinkTraktAccount(
    private val accountStore: AccountStore,
    private val unlinkFromTrakt: UnlinkFromTrakt
) : UnlinkTraktAccount {

    override suspend operator fun invoke() {
        unlinkFromTrakt()
        accountStore.clear()
    }
}

class FakeUnlinkTraktAccount : UnlinkTraktAccount {

    var invoked = false
        private set

    override suspend operator fun invoke() {
        invoked = true
    }
}
