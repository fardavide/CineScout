package entities.suggestions

import entities.Actor
import entities.FiveYearRange
import entities.Genre

data class SuggestionData(
    val actors: Collection<Actor>,
    val genres: Collection<Genre>,
    val years: Collection<FiveYearRange>
)
