package cinescout

import korlibs.time.DateTime
import org.koin.core.annotation.Factory

interface GetCurrentDateTime {

    operator fun invoke(): DateTime
}

@Factory
internal class RealGetCurrentDateTime : GetCurrentDateTime {

    override fun invoke(): DateTime = DateTime.now()
}

class FakeGetCurrentDateTime(private val dateTime: DateTime) : GetCurrentDateTime {

    override fun invoke() = dateTime
}
