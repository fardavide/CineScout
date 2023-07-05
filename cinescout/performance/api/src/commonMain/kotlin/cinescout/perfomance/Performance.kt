package cinescout.perfomance

import cinescout.CineScoutTestApi
import cinescout.notImplementedFake

interface Performance {

    suspend fun <T> trace(traceName: String, block: suspend (Trace) -> T): T
}

@CineScoutTestApi
class FakePerformance : Performance {

    override suspend fun <T> trace(traceName: String, block: suspend (Trace) -> T): T = notImplementedFake()
}
