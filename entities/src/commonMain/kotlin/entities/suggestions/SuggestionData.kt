package entities.suggestions

import entities.model.Actor
import entities.model.FiveYearRange
import entities.model.Genre

data class SuggestionData(
    val actors: Collection<Actor>,
    val genres: Collection<Genre>,
    val years: Collection<FiveYearRange>
)
