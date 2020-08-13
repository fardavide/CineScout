import Test.Actor.DenzelWashington
import Test.Actor.JohnnyDepp
import Test.Genre.Horror
import Test.Genre.War
import assert4k.*
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
