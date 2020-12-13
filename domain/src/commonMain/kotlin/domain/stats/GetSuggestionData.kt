package domain.stats

import entities.stats.StatRepository
import entities.suggestions.SuggestionData
import entities.suggestions.SuggestionData_Either

class GetSuggestionData(
    private val stats: StatRepository
) {

    suspend operator fun invoke(limit: Int): SuggestionData_Either =
        SuggestionData(stats.topActors(limit), stats.topGenres(limit), stats.topYears(limit))
}
