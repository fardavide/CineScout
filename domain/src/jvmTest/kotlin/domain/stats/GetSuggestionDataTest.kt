package domain.stats

import assert4k.*
import domain.StubStatRepository
import domain.Test.Actor.DenzelWashington
import domain.Test.Actor.JohnnyDepp
import domain.Test.Genre.Horror
import domain.Test.Genre.War
import domain.stats.GetSuggestionData
import entities.model.FiveYearRange
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.*

internal class GetSuggestionDataTest {

    private val getSuggestion = GetSuggestionData(
        stats = StubStatRepository()
    )

    @Test
    fun `most liked actors`() = runBlockingTest {
        val result = getSuggestion(2).rightOrThrow().actors

        assert that result *{
            it contains JohnnyDepp
            it contains DenzelWashington
        }
    }

    @Test
    fun `most liked genres`() = runBlockingTest {
        val result = getSuggestion(2).rightOrThrow().genres

        assert that result *{
            it contains War
            it contains Horror
        }
    }

    @Test
    fun `most liked years`() = runBlockingTest {
        val result = getSuggestion(2).rightOrThrow().years

        assert that result *{
            it contains FiveYearRange(2020u)
            it contains FiveYearRange(2015u)
        }
    }

}
