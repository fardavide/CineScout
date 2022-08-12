package tests

import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.GenerateSuggestedMovies
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.test.inject
import util.BaseAppTest
import util.BaseTmdbTest
import util.BaseTraktTest
import kotlin.test.Ignore
import kotlin.test.Test

class AppTest : BaseAppTest(), BaseTmdbTest, BaseTraktTest {

    private val generateSuggestedMovies: GenerateSuggestedMovies by inject()

    @Test
    @Ignore
    fun test() = runBlocking {
        givenSuccessfullyLinkedToTmdb()
        givenSuccessfullyLinkedToTrakt()
        println(generateSuggestedMovies(SuggestionsMode.Deep).first())
    }
}
