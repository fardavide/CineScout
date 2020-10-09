package domain.auth

import kotlin.test.*
import entities.auth.TmdbAuth
import util.test.CoroutinesTest
import entities.left
import io.mockk.*
import domain.Test.EmailAddress
import entities.NotBlankString
import kotlin.test.*
import assert4k.*
import entities.requireValid
import kotlinx.coroutines.test.*

class LinkToTmdbTest : CoroutinesTest {

    private val auth = mockk<TmdbAuth>()
    private val link = LinkToTmdb(auth)

    @Test
    fun `WrongCredentials is returned if wrong credentials`() = coroutinesTest {
        val expected = TmdbAuth.LoginError.WrongCredentials.left()
        coEvery { auth.login(any(), any()) } returns expected

        assert that link(EmailAddress.Some, NotBlankString("psw").requireValid()) equals expected
    }
}
