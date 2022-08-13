package cinescout.auth.trakt.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.account.trakt.domain.usecase.SyncTraktAccount
import cinescout.auth.trakt.domain.TraktAuthRepository
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.StartUpdateSuggestedMovies
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class LinkToTraktTest {

    private val startUpdateSuggestedMovies: StartUpdateSuggestedMovies = mockk(relaxUnitFun = true)
    private val syncTraktAccount: SyncTraktAccount = mockk(relaxUnitFun = true)
    private val traktAuthRepository: TraktAuthRepository = mockk {
        every { link() } returns flowOf(LinkToTrakt.State.Success.right())
    }
    private val linkToTrakt = LinkToTrakt(
        startUpdateSuggestedMovies = startUpdateSuggestedMovies,
        syncTraktAccount = syncTraktAccount,
        traktAuthRepository = traktAuthRepository
    )

    @Test
    fun `correctly calls repository`() = runTest {
        // given
        linkToTrakt().test {

            // when
            cancelAndConsumeRemainingEvents()

            // then
            coVerifySequence {
                traktAuthRepository.link()
                syncTraktAccount()
                startUpdateSuggestedMovies(SuggestionsMode.Quick)
            }
        }
    }
}
