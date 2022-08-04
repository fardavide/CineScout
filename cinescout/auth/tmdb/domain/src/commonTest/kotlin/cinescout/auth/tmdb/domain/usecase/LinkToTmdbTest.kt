package cinescout.auth.tmdb.domain.usecase

import cinescout.account.tmdb.domain.usecase.SyncTmdbAccount
import cinescout.auth.tmdb.domain.TmdbAuthRepository
import cinescout.movies.domain.usecase.SyncRatedMovies
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class LinkToTmdbTest {

    private val syncRatedMovies: SyncRatedMovies = mockk(relaxUnitFun = true)
    private val syncTmdbAccount: SyncTmdbAccount = mockk(relaxUnitFun = true)
    private val tmdbAuthRepository: TmdbAuthRepository = mockk(relaxed = true)
    private val linkToTmdb = LinkToTmdb(
        syncRatedMovies = syncRatedMovies,
        syncTmdbAccount = syncTmdbAccount,
        tmdbAuthRepository = tmdbAuthRepository
    )

    @Test
    fun `correctly calls repository`() = runTest {
        linkToTmdb()
        verify { tmdbAuthRepository.link() }
    }
}
