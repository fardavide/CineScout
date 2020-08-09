package suggestions

import FiveYearRange
import Name

data class Suggestion(
    val actors: Collection<Name>,
    val genres: Collection<Name>,
    val years: Collection<FiveYearRange>
)
