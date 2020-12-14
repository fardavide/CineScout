package util.test

import kotlinx.coroutines.test.DelayController
import kotlin.time.Duration

fun DelayController.advanceTimeBy(delay: Duration): Long =
    advanceTimeBy(delay.toLongMilliseconds())
