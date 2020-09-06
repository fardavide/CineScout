package domain

import entities.movies.DiscoverParams
import entities.suggestions.SuggestionData
import util.useIfTrue
import kotlin.random.Random

class GenerateDiscoverParams(private val randomize: Boolean = true) {

    operator fun invoke(suggestionData: SuggestionData) = DiscoverParams(
        actor = suggestionData.actors.randomOrFirst().id,
        genre = suggestionData.genres.randomOrFirst().id,
        year = randomBooleanOrFalse().useIfTrue { suggestionData.years.randomOrFirst().range.randomOrFirst().toInt() }
    )

    private fun <T> Collection<T>.randomOrFirst() =
        if (randomize) random()
        else first()

    private fun UIntRange.randomOrFirst() =
        if (randomize) random()
        else first()

    private fun randomBooleanOrFalse() =
        if (randomize) Random.nextBoolean()
        else false
}
