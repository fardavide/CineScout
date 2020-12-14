package movies.remote.tmdb

import assert4k.*
import entities.left
import entities.movies.SearchError
import io.mockk.mockk
import movies.remote.tmdb.mapper.MovieDetailsMapper
import util.test.CoroutinesTest
import kotlin.test.*

class TmdbRemoteMovieSourceImplTest : CoroutinesTest {

    private val source = TmdbRemoteMovieSourceImpl(
        movieDiscoverService = mockk(),
        movieService = mockk(),
        movieSearchService = mockk(),
        moviePageResultMapper = mockk(),
        movieDetailsMapper = MovieDetailsMapper()
    )

    @Test
    fun `returns EmptyQuery if query is empty`() = coroutinesTest {
        val result = source.search("")
        assert that result equals SearchError.EmptyQuery.left()
    }

    @Test
    fun `returns ShortQuery if query is 1 char`() = coroutinesTest {
        val result = source.search("a")
        assert that result equals SearchError.ShortQuery.left()
    }
}
