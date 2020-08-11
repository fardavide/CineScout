package suggestions

import FiveYearRange
import Name

data class SuggestionData(
    val actors: Collection<Name>,
    val genres: Collection<Name>,
    val years: Collection<FiveYearRange>
)
