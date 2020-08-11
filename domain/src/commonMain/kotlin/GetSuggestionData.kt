import stats.StatRepository
import suggestions.SuggestionData

class GetSuggestionData(
    private val stats: StatRepository
) {

    suspend operator fun invoke(limit: UInt): SuggestionData =
        SuggestionData(stats.topActors(limit), stats.topGenres(limit), stats.topYears(limit))
}
