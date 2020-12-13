@file:Suppress("DataClassPrivateConstructor")

package entities.suggestions

import entities.Either
import entities.Validable
import entities.Validable.Companion.validate
import entities.ValidationError
import entities.Validator
import entities.left
import entities.model.Actor
import entities.model.FiveYearRange
import entities.model.Genre
import entities.right

data class SuggestionData private constructor(
    val actors: Collection<Actor>,
    val genres: Collection<Genre>,
    val years: Collection<FiveYearRange>

) : Validable<EmptySuggestionDataError> by (Validator {
    if (actors.isEmpty() || genres.isEmpty()) EmptySuggestionDataError.left()
    else SuggestionData(actors, genres, years).right()
}) {

    companion object {

        operator fun invoke(
            actors: Collection<Actor>,
            genres: Collection<Genre>,
            years: Collection<FiveYearRange>
        ) = SuggestionData(actors, genres, years).validate()
    }
}

object EmptySuggestionDataError : ValidationError

typealias SuggestionData_Either = Either<EmptySuggestionDataError, SuggestionData>
