package cinescout.suggestions.domain.model

@JvmInline
value class Affinity private constructor(val value: Int) {

    companion object {

        fun from(source: SuggestionSource): Affinity = when (source) {
            is SuggestionSource.FromLiked -> Affinity(60)
            is SuggestionSource.FromRated -> Affinity(source.rating.intValue * 9)
            is SuggestionSource.FromWatchlist -> Affinity(70)
            is SuggestionSource.PersonalSuggestions -> Affinity(90)
            SuggestionSource.Popular,
            SuggestionSource.Recommended,
            SuggestionSource.Trending,
            SuggestionSource.Anticipated -> Affinity(80)
        }

        fun of(value: Int): Affinity {
            check(value in 0..100)
            return Affinity(value)
        }
    }
}
