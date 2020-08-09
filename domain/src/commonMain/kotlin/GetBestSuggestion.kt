import stats.StatRepository
import suggestions.Suggestion

class GetBestSuggestion(
    private val stats: StatRepository
) {

    suspend operator fun invoke(limit: UInt): Suggestion =
        Suggestion(stats.topActors(limit), stats.topGenres(limit), stats.topYears(limit))
}
