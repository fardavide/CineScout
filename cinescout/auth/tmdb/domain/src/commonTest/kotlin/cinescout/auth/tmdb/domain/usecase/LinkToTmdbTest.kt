package cinescout.auth.tmdb.domain.usecase

import cinescout.auth.tmdb.domain.TmdbAuthRepository
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class LinkToTmdbTest {

    private val tmdbAuthRepository: TmdbAuthRepository = mockk(relaxed = true)
    private val linkToTmdb = LinkToTmdb(tmdbAuthRepository)

    @Test
    fun `correctly calls repository`() = runTest {
        linkToTmdb()
        verify { tmdbAuthRepository.link() }
    }
}
