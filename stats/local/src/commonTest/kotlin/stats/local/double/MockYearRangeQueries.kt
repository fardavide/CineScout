package stats.local.double

import database.movies.YearRangeQueries
import entities.FiveYearRange
import io.mockk.every
import io.mockk.mockk

fun mockYearRangeQueries(): YearRangeQueries = mockk {

    val years = mutableSetOf<FiveYearRange>()

    every { insert(any()) } answers {
        years += FiveYearRange(firstArg())
    }

    every { lastInsertRowId().executeAsOne() } answers { years.size - 1.toLong() }
}
