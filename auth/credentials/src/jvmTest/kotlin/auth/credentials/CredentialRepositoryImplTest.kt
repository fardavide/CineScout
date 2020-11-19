package auth.credentials

import assert4k.*
import database.credentials.TmdbCredentialQueries
import entities.TmdbStringId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import util.test.CoroutinesTest
import kotlin.test.*

class CredentialRepositoryImplTest : CoroutinesTest {

    private val mockTmdbCredentialQueries = mockk<TmdbCredentialQueries> {
        var accountId: TmdbStringId? = null
        var token: String? = null
        every { insert(TmdbStringId(any()), any(), any()) } answers {
            accountId = TmdbStringId(firstArg())
            token = secondArg()
        }
        every { selectAccessToken() } answers {
            mockk {
                every { executeAsOneOrNull() } answers { token }
            }
        }
        every { selectAccountId() } answers {
            mockk {
                every { executeAsOneOrNull() } answers { accountId }
            }
        }
        every { delete() } answers { token = null }
    }
    private val repository =
        CredentialRepositoryImpl(tmdbCredentials = mockTmdbCredentialQueries)

    @Test
    fun `database is not called if there is a cached tmdbToken and it didn't change`() = coroutinesTest {
        repository.run {
            storeTmdbCredentials(TmdbStringId("accountId"), "token", "sessionId")
            findTmdbAccessTokenBlocking()
            findTmdbAccessTokenBlocking()

            verify(exactly = 0) { mockTmdbCredentialQueries.selectAccessToken() }
        }
    }

    @Test
    fun `new tmdbToken is returned after update`() = coroutinesTest {
        repository.run {
            storeTmdbCredentials(TmdbStringId("accountId"), "token1", "sessionId")
            storeTmdbCredentials(TmdbStringId("accountId"), "token2", "sessionId")

            assert that findTmdbAccessTokenBlocking() equals "token2"
        }
    }

    @Test
    fun `tmdbToken can be deleted correctly`() = coroutinesTest {
        repository.run {
            storeTmdbCredentials(TmdbStringId("accountId"), "token1", "sessionId")
            deleteTmdbAccessToken()

            assert that findTmdbAccessTokenBlocking() `is` Null
        }
    }
}
