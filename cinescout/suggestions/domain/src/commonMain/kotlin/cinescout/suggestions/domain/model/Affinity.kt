package cinescout.suggestions.domain.model

@JvmInline
value class Affinity private constructor(val value: Int) {

    companion object {

        fun from(source: SuggestionSource): Affinity = when (source) {
            SuggestionSource.FromLiked -> Affinity(80)
            is SuggestionSource.FromRated -> Affinity(source.rating.intValue * 10)
            SuggestionSource.FromWatchlist -> Affinity(70)
            SuggestionSource.PersonalSuggestions -> Affinity(80)
            SuggestionSource.Popular,
            SuggestionSource.Suggested,
            SuggestionSource.Trending,
            SuggestionSource.Upcoming -> Affinity(60)
        }
    }
}
