package cinescout.auth.trakt.domain.usecase

import cinescout.auth.trakt.domain.TraktAuthRepository
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class LinkToTraktTest {

    private val traktAuthRepository: TraktAuthRepository = mockk(relaxed = true)
    private val linkToTrakt = LinkToTrakt(traktAuthRepository)

    @Test
    fun `correctly calls repository`() = runTest {
        linkToTrakt()
        verify { traktAuthRepository.link() }
    }
}
