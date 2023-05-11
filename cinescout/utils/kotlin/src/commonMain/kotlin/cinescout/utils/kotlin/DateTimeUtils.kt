package cinescout.utils.kotlin

import korlibs.time.TimeSpan
import kotlin.time.Duration

fun Duration.toTimeSpan() = TimeSpan(this.inWholeMilliseconds.toDouble())
