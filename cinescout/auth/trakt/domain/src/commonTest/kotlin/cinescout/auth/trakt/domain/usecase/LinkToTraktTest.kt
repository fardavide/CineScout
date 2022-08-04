package cinescout.auth.trakt.domain.usecase

import cinescout.account.trakt.domain.usecase.SyncTraktAccount
import cinescout.auth.trakt.domain.TraktAuthRepository
import cinescout.movies.domain.usecase.SyncRatedMovies
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class LinkToTraktTest {

    private val syncRatedMovies: SyncRatedMovies = mockk(relaxUnitFun = true)
    private val syncTmdbAccount: SyncTraktAccount = mockk(relaxUnitFun = true)
    private val traktAuthRepository: TraktAuthRepository = mockk(relaxed = true)
    private val linkToTrakt = LinkToTrakt(
        syncRatedMovies = syncRatedMovies,
        syncTraktAccount = syncTmdbAccount,
        traktAuthRepository = traktAuthRepository
    )

    @Test
    fun `correctly calls repository`() = runTest {
        linkToTrakt()
        verify { traktAuthRepository.link() }
    }
}
