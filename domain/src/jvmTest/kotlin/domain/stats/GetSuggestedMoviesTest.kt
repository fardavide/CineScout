package domain.stats

import assert4k.*
import domain.MockStatRepository
import domain.Test.Movie.Blow
import domain.Test.Movie.TheBookOfEli
import entities.stats.StatRepository
import entities.stats.StatRepository.Companion.STORED_SUGGESTIONS_LIMIT
import io.mockk.*
import kotlinx.coroutines.flow.emptyFlow
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
            )
        }
    )

    @Test
    fun `movies are added to suggestions if limit is not reached`() = coroutinesTest {

    }

    @Test
    fun `movies are not added to suggestions if limit is reached`() = coroutinesTest {

    }
}
