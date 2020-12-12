package domain.stats

import entities.model.FiveYearRange
import entities.movies.DiscoverParams
import entities.suggestions.SuggestionData
import util.takeIfNotEmpty
import util.useIfTrue
import kotlin.random.Random

class GenerateDiscoverParams(private val randomize: Boolean = true) {

    operator fun invoke(suggestionData: SuggestionData): DiscoverParams {
        val shouldUseYear = suggestionData.years.isNotEmpty() && randomBooleanOrFalse()
        return DiscoverParams(
            actor = suggestionData.actors.randomOrFirst().id,
            genre = suggestionData.genres.randomOrFirst().id,
            year = shouldUseYear.useIfTrue { suggestionData.years.randomOrFirstYear() }
        )
    }

    private fun <T> Collection<T>.randomOrFirst(): T =
        if (randomize) random()
        else first()

    private fun Collection<FiveYearRange>.randomOrFirstYear(): Int =
        randomOrFirst().range.randomOrFirst().toInt()

    private fun UIntRange.randomOrFirst() =
        if (randomize) random()
        else first()

    private fun randomBooleanOrFalse() =
        if (randomize) Random.nextBoolean()
        else false
}
