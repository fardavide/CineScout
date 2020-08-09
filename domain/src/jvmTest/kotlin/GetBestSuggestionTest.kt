import assert4k.*
import kotlinx.coroutines.test.runBlockingTest
import stats.StatRepository
import kotlin.test.Test

internal class GetBestSuggestionTest {

    private val getSuggestion = GetBestSuggestion(
        stats = StubStatRepository()
    )

    @Test
    fun `most liked actors`() = runBlockingTest {
        val result = getSuggestion(2u).actors

        assert that result *{
            it contains Name("Johnny Depp")
            it contains Name("Denzel Washington")
        }
    }

    @Test
    fun `most liked genres`() = runBlockingTest {
        val result = getSuggestion(2u).genres

        assert that result *{
            it contains Name("War")
            it contains Name("Horror")
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
