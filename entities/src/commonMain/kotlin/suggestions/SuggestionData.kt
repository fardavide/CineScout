package suggestions

import Actor
import FiveYearRange
import Name

data class SuggestionData(
    val actors: Collection<Actor>,
    val genres: Collection<Name>,
    val years: Collection<FiveYearRange>
)
