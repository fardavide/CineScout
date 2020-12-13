package domain.stats

import assert4k.*
import co.touchlab.kermit.CommonLogger
import domain.Test.Movie.Blow
import domain.Test.Movie.TheBookOfEli
import entities.right
import entities.stats.StatRepository
import io.mockk.*
import kotlin.test.*
import util.test.*

internal class GetSuggestedMoviesTest : CoroutinesTest {

    private val stats: StatRepository = mockk()
    private val getSuggestedMovies = GetSuggestedMovies(
        stats = stats,
        generateMoviesSuggestions = mockk {
            coEvery { this@mockk() } returns listOf(
                Blow,
                TheBookOfEli
            ).right()
        },
        logger = CommonLogger()
    )

    @Test
    fun `movies are added to suggestions if limit is not reached`() = coroutinesTest {

    }

    @Test
    fun `movies are not added to suggestions if limit is reached`() = coroutinesTest {

    }
}
