package cinescout.tvshows.data.remote

import arrow.core.left
import arrow.core.right
import cinescout.model.NetworkOperation
import cinescout.network.DualSourceCall
import cinescout.test.kotlin.TestTimeout
import cinescout.tvshows.domain.testdata.TvShowTestData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import store.Paging
import store.builder.dualSourcesPagedDataOf
import store.builder.pagedDataOf
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RealRemoteTvShowDataSourceTest {

    private val dualSourceCall = DualSourceCall(isFirstSourceLinked = { true }, isSecondSourceLinked = { true })
    private val tmdbSource: TmdbRemoteTvShowDataSource = mockk(relaxUnitFun = true)
    private val traktSource: TraktRemoteTvShowDataSource = mockk(relaxUnitFun = true)
    private val remoteTvShowDataSource = RealRemoteTvShowDataSource(
        dualSourceCall = dualSourceCall,
        tmdbSource = tmdbSource,
        traktSource = traktSource
    )

    @Test
    fun `get watchlist delivers data when only Tmdb is linked`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val tvShow = TvShowTestData.Grimm
        val expected = dualSourcesPagedDataOf(TvShowTestData.Grimm.tmdbId).right()
        coEvery { tmdbSource.getWatchlistTvShows(page = any()) } returns pagedDataOf(tvShow).right()
        coEvery { traktSource.getWatchlistTvShows(page = any()) } returns NetworkOperation.Skipped.left()

        // when
        val result = remoteTvShowDataSource.getWatchlistTvShows(Paging.Page.DualSources.Initial)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get watchlist delivers data when only Trakt is linked`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val tvShow = TvShowTestData.Grimm
        val expected = dualSourcesPagedDataOf(TvShowTestData.Grimm.tmdbId).right()
        coEvery { tmdbSource.getWatchlistTvShows(page = any()) } returns NetworkOperation.Skipped.left()
        coEvery { traktSource.getWatchlistTvShows(page = any()) } returns pagedDataOf(tvShow.tmdbId).right()

        // when
        val result = remoteTvShowDataSource.getWatchlistTvShows(Paging.Page.DualSources.Initial)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get watchlist skips when none of Tmdb and Trakt are linked`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val expected = NetworkOperation.Skipped.left()
        coEvery { tmdbSource.getWatchlistTvShows(page = any()) } returns NetworkOperation.Skipped.left()
        coEvery { traktSource.getWatchlistTvShows(page = any()) } returns NetworkOperation.Skipped.left()

        // when
        val result = remoteTvShowDataSource.getWatchlistTvShows(Paging.Page.DualSources.Initial)

        // then
        assertEquals(expected, result)
    }
}
