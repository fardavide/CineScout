package entities.suggestions

import entities.field.Actor
import entities.field.FiveYearRange
import entities.field.Genre

data class SuggestionData(
    val actors: Collection<Actor>,
    val genres: Collection<Genre>,
    val years: Collection<FiveYearRange>
)
