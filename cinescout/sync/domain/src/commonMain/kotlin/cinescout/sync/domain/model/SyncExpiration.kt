package cinescout.sync.domain.model

import kotlin.time.Duration.Companion.minutes

internal object SyncExpiration {

    val Ratings = 5.minutes
    val Watchlist = 5.minutes
}
