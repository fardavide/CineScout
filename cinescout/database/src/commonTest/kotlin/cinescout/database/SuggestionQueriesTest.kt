package cinescout.database

import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.sample.DatabaseSuggestionSample
import cinescout.database.sample.DatabaseTvShowSample
import cinescout.test.database.TestDatabaseExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe

class SuggestionQueriesTest : BehaviorSpec({
    val testDatabaseExtension = extension(TestDatabaseExtension())

    fun database() = testDatabaseExtension.database
    fun suggestionQueries() = database().suggestionQueries

    Given("movie suggestions") {
        val suggestions = listOf(
            DatabaseSuggestionSample.War,
            DatabaseSuggestionSample.Inception,
            DatabaseSuggestionSample.TheWolfOfWallStreet
        )
        for (suggestion in suggestions) {
            suggestionQueries().insert(suggestion)
        }

        When("find all") {
            val result = suggestionQueries().findAllMovies().executeAsList()

            Then("suggestions are sorted by affinity") {
                result shouldBe suggestions.sortedByDescending { it.affinity }
            }
        }

        And("liked and disliked movies") {
            with(database().likedMovieQueries) {
                insert(DatabaseMovieSample.Inception.tmdbId, isLiked = true)
                insert(DatabaseMovieSample.War.tmdbId, isLiked = false)
            }

            When("find all not known") {
                val result = suggestionQueries().findAllNotKnownMovies().executeAsList()

                Then("liked and disliked movies are not included") {
                    result shouldContainOnly listOf(DatabaseSuggestionSample.TheWolfOfWallStreet)
                }
            }
        }
    }

    Given("tv show suggestions") {
        val suggestions = listOf(
            DatabaseSuggestionSample.Grimm,
            DatabaseSuggestionSample.Dexter,
            DatabaseSuggestionSample.BreakingBad
        )
        for (suggestion in suggestions) {
            suggestionQueries().insert(suggestion)
        }

        When("find all") {
            val result = suggestionQueries().findAllTvShows().executeAsList()

            Then("suggestions are sorted by affinity") {
                result shouldBe suggestions.sortedByDescending { it.affinity }
            }
        }

        And("liked and disliked tv shows") {
            with(database().likedTvShowQueries) {
                insert(DatabaseTvShowSample.BreakingBad.tmdbId, isLiked = true)
                insert(DatabaseTvShowSample.Grimm.tmdbId, isLiked = false)
            }

            When("find all not known") {
                val result = suggestionQueries().findAllNotKnownTvShows().executeAsList()

                Then("liked and disliked tv shows are not included") {
                    result shouldContainOnly listOf(DatabaseSuggestionSample.Dexter)
                }
            }
        }
    }
})
