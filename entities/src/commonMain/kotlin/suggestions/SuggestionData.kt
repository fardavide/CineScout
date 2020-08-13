package suggestions

import Actor
import FiveYearRange
import Genre

data class SuggestionData(
    val actors: Collection<Actor>,
    val genres: Collection<Genre>,
    val years: Collection<FiveYearRange>
)
