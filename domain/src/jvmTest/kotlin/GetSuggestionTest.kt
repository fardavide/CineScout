import assert4k.*
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test

internal class GetSuggestionTest {

    private val getSuggestion = GetSuggestion()

    @Test
    fun `most liked actor`() = runBlockingTest {
        assert that getSuggestion() *{
            +actor.s equals "Johnny Depp"
        }
    }

}
