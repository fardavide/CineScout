import assert4k.assert
import assert4k.contains
import assert4k.that
import assert4k.times
import domain.GetSuggestionData
import domain.StubStatRepository
import domain.Test.Actor.DenzelWashington
import domain.Test.Actor.JohnnyDepp
import domain.Test.Genre.Horror
import domain.Test.Genre.War
import entities.FiveYearRange
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test

internal class GetSuggestionDataTest {

    private val getSuggestion = GetSuggestionData(
        stats = StubStatRepository()
    )

    @Test
    fun `most liked actors`() = runBlockingTest {
        val result = getSuggestion(2u).actors

        assert that result *{
            it contains JohnnyDepp
            it contains DenzelWashington
        }
    }

    @Test
    fun `most liked genres`() = runBlockingTest {
        val result = getSuggestion(2u).genres

        assert that result *{
            it contains War
            it contains Horror
        }
    }

    @Test
    fun `most liked years`() = runBlockingTest {
        val result = getSuggestion(2u).years

        assert that result *{
            it contains FiveYearRange(2020u)
            it contains FiveYearRange(2015u)
        }
    }

}
