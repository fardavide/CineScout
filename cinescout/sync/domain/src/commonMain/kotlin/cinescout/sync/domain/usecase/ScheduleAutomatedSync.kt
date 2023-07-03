package cinescout.sync.domain.usecase

import cinescout.CineScoutTestApi

interface ScheduleAutomatedSync {

    operator fun invoke()
}

@CineScoutTestApi
class FakeScheduleAutomatedSync : ScheduleAutomatedSync {

    override fun invoke() = Unit
}
