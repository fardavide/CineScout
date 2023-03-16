package cinescout.utils.kotlin

import com.soywiz.klock.TimeSpan
import kotlin.time.Duration

fun Duration.toTimeSpan() = TimeSpan(this.inWholeMilliseconds.toDouble())
