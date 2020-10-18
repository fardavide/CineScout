package domain.auth

import assert4k.*
import domain.Test.EmailAddress
import entities.NotBlankString
import entities.auth.TmdbAuth
import entities.left
import io.mockk.coEvery
import io.mockk.mockk
import util.test.CoroutinesTest
import kotlin.test.*

class LinkToTmdbTest : CoroutinesTest {

    private val auth = mockk<TmdbAuth>()
    private val link = LinkToTmdb(auth)

    @Test
    fun `WrongCredentials is returned if wrong credentials`() = coroutinesTest {
        val expected = TmdbAuth.LoginError.WrongCredentials.left()
        coEvery { auth.login(any(), any()) } returns expected

        assert that link(EmailAddress.Some, NotBlankString("psw")) equals expected
    }
}
