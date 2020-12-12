package stats.local.double

import database.movies.Movie
import database.stats.StatType
import database.stats.SuggestionQueries
import entities.IntId
import entities.TmdbId
import io.mockk.every
import io.mockk.mockk

fun mockSuggestionQueries(
    suggestions: MutableSet<IntId>
): SuggestionQueries = mockk {

    every { insert(IntId(any())) } answers {
        val idArg = IntId(firstArg())
        suggestions += idArg
    }

    every { delete(IntId(any())) } answers {
        val idArg = IntId(firstArg())
        suggestions -= idArg
    }
}
