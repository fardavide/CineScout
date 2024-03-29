package cinescout.database

import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.sample.DatabaseSuggestionSample
import cinescout.test.database.TestDatabaseExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class SuggestionQueriesTest : BehaviorSpec({
    val testDatabaseExtension = extension(TestDatabaseExtension())

    fun database() = testDatabaseExtension.database
    fun suggestionQueries() = database().suggestionQueries

    Given("suggestions") {
        val suggestions = listOf(
            DatabaseSuggestionSample.BreakingBad,
            DatabaseSuggestionSample.Inception,
            DatabaseSuggestionSample.TheWolfOfWallStreet
        )
        for (suggestion in suggestions) {
            suggestionQueries().insert(suggestion)
        }

        When("find all") {
            val result = suggestionQueries().findAllNotKnown().executeAsList()

            Then("suggestions are sorted by affinity") {
                result shouldBe suggestions.sortedByDescending { it.affinity }
            }
        }

        And("liked and disliked") {
            with(database().votingQueries) {
                insert(DatabaseMovieSample.Inception.traktId, DatabaseMovieSample.Inception.tmdbId, isLiked = true)
                insert(DatabaseMovieSample.War.traktId, DatabaseMovieSample.War.tmdbId, isLiked = false)
            }

            When("find all not known") {
                val result = suggestionQueries().findAllNotKnown().executeAsList()

                Then("liked and disliked are not included") {
                    result shouldBe listOf(
                        DatabaseSuggestionSample.BreakingBad,
                        DatabaseSuggestionSample.TheWolfOfWallStreet
                    )
                }
            }
        }
    }
})
