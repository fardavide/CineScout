package auth.credentials

import assert4k.*
import database.credentials.TmdbCredentialQueries
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import util.test.CoroutinesTest
import kotlin.test.*

class CredentialRepositoryImplTest : CoroutinesTest {

    private val mockTmdbCredentialQueries = mockk<TmdbCredentialQueries> {
        var token: String? = null
        every { insert(any()) } answers { token = firstArg() }
        every { select() } answers {
            mockk {
                every { executeAsOneOrNull() } answers { token }
            }
        }
        every { delete() } answers { token = null }
    }
    private val repository =
        CredentialRepositoryImpl(tmdbCredentials = mockTmdbCredentialQueries)

    @Test
    fun `database is not called if there is a cached tmdbToken and it didn't change`() = coroutinesTest {
        repository.run {
            storeTmdbAccessToken("token")
            findTmdbAccessTokenBlocking()
            findTmdbAccessTokenBlocking()

            verify(exactly = 0) { mockTmdbCredentialQueries.select() }
        }
    }

    @Test
    fun `new tmdbToken is returned after update`() = coroutinesTest {
        repository.run {
            storeTmdbAccessToken("token1")
            storeTmdbAccessToken("token2")

            assert that findTmdbAccessTokenBlocking() equals "token2"
        }
    }

    @Test
    fun `tmdbToken can be deleted correctly`() = coroutinesTest {
        repository.run {
            storeTmdbAccessToken("token1")
            deleteTmdbAccessToken()

            assert that findTmdbAccessTokenBlocking() `is` Null
        }
    }
}
