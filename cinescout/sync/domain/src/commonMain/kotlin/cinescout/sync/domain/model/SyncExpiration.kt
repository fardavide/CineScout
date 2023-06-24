package cinescout.sync.domain.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

object SyncExpiration {

    val Search = 3.days
}

internal val NoSyncExpiration = Duration.INFINITE
