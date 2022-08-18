package store

import kotlin.time.DurationUnit.MINUTES
import kotlin.time.toDuration

val DefaultRefreshInterval = 3.toDuration(MINUTES)
