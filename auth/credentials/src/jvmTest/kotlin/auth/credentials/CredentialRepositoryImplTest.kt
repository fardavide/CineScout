package auth.credentials

import assert4k.*
import database.credentials.TmdbCredentialQueries
import database.credentials.TraktCredentialQueries
import entities.TmdbId
import entities.TmdbStringId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import util.test.CoroutinesTest
import kotlin.test.*

class CredentialRepositoryImplTest : CoroutinesTest {

    private val mockTmdbCredentialQueries = mockk<TmdbCredentialQueries> {
        var v3accountId: TmdbId? = null
        var v4accountId: TmdbStringId? = null
        var token: String? = null
        every { insert(TmdbStringId(any()), any(), any()) } answers {
            v4accountId = TmdbStringId(firstArg())
            token = secondArg()
        }
        every { insertV3accountId(TmdbId(any())) } answers {
            v3accountId = TmdbId(firstArg())
        }
        every { selectAccessToken() } answers {
            mockk {
                every { executeAsOneOrNull() } answers { token }
            }
        }
        every { selectV4accountId() } answers {
            mockk {
                every { executeAsOneOrNull() } answers { v4accountId }
            }
        }
        every { selectV3accountId() } answers {
            mockk {
                every { executeAsOneOrNull() } answers { v3accountId }
            }
        }
        every { delete() } answers {
            v3accountId = null
            v4accountId = null
            token = null
        }
    }
    private val mockTraktCredentialQueries = mockk<TraktCredentialQueries> {
        var token: String? = null
        every { insert(any()) } answers {
            token = firstArg()
        }
        every { selectAccessToken() } answers {
            mockk {
                every { executeAsOneOrNull() } answers { token }
            }
        }
        every { delete() } answers { token = null }
    }
    private val repository =
        CredentialRepositoryImpl(
            tmdbCredentials = mockTmdbCredentialQueries,
            traktCredentials = mockTraktCredentialQueries
        )

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
