package cinescout.auth.tmdb.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.account.tmdb.domain.usecase.SyncTmdbAccount
import cinescout.auth.tmdb.domain.TmdbAuthRepository
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.StartUpdateSuggestedMovies
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class LinkToTmdbTest {

    private val startUpdateSuggestedMovies: StartUpdateSuggestedMovies = mockk(relaxUnitFun = true)
    private val syncTmdbAccount: SyncTmdbAccount = mockk(relaxUnitFun = true)
    private val tmdbAuthRepository: TmdbAuthRepository = mockk {
        every { link() } returns flowOf(LinkToTmdb.State.Success.right())
    }
    private val linkToTmdb = LinkToTmdb(
        startUpdateSuggestedMovies = startUpdateSuggestedMovies,
        syncTmdbAccount = syncTmdbAccount,
        tmdbAuthRepository = tmdbAuthRepository
    )

    @Test
    fun `correctly calls repository`() = runTest {
        // given
        linkToTmdb().test {

            // when
            cancelAndConsumeRemainingEvents()

            // then
            coVerifySequence {
                tmdbAuthRepository.link()
                syncTmdbAccount()
                startUpdateSuggestedMovies(SuggestionsMode.Quick)
            }
        }
    }
}
