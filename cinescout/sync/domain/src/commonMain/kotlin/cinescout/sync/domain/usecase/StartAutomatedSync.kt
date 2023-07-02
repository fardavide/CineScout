package cinescout.sync.domain.usecase

import cinescout.CineScoutTestApi

interface StartAutomatedSync {

    operator fun invoke()
}

@CineScoutTestApi
class FakeStartAutomatedSync : StartAutomatedSync {

    override fun invoke() = Unit
}
