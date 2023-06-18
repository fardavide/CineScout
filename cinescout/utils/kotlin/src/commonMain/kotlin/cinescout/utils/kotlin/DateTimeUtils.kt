package cinescout.utils.kotlin

import korlibs.time.Date
import korlibs.time.TimeSpan
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.Duration
import java.time.LocalDate as JavaLocalDate

/**
 * @return String representation of the date using [style] as [FormatStyle] for the current locale.
 * Examples:
 *  * 'Tuesday, August 20, 1991' for [FormatStyle.FULL]
 *  * 'August 20, 1991' for [FormatStyle.LONG]
 *  * 'Aug 20, 1991' for [FormatStyle.MEDIUM]
 *  * '8/20/91' for [FormatStyle.SHORT]
 */
fun Date.formatLocalDate(style: FormatStyle): String =
    DateTimeFormatter.ofLocalizedDate(style).format(toJavaLocalDate())

private fun Date.toJavaLocalDate() = JavaLocalDate.of(year, month1, day)

fun Duration.toTimeSpan() = TimeSpan(this.inWholeMilliseconds.toDouble())
